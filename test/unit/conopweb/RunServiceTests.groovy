package conopweb

import grails.test.mixin.*
import org.junit.*

@TestFor(RunService)
class RunServiceTests {
	def mongoService, datasetService, eventService

	@Before
	void setup() {
		mongoService = new MongoService('localhost', 'conopweb_test')
		service.mongoService = mongoService

		eventService = new EventService()
		eventService.mongoService = mongoService
		eventService.collection.remove([:])

		datasetService = new DatasetService()
		datasetService.mongoService = mongoService
		datasetService.eventService = eventService
		datasetService.collection.remove([:])

		service.datasetService = datasetService
		service.eventService = eventService
	}

	@After
	void teardown() {
		service.collection.remove([:])
		service.progressCollection.remove([:])
		datasetService.collection.remove([:])
		eventService.collection.remove([:])
	}

	void testFind() {
		assert [] == service.find()

		service.collection.add(id: 'test', name: 'Test Run', dataset: 'dataset', simulation: [:])

		def list = service.find()
		assert list
		assert 1 == list.size()

		def run = list[0]
		assert 'test' == run.id
		assert 'Test Run' == run.name
		assert 'dataset' == run.dataset
		assert [:] == run.simulation
	}

	void testGet() {
		assert null == service.get(null)
		assert null == service.get('')
		assert null == service.get('test')

		service.collection.add(id: 'test', name: 'Test Run', dataset: 'dataset', simulation: [:])

		def run = service.get('Test')
		assert run
		assert 'test' == run.id
		assert 'Test Run' == run.name
		assert 'dataset' == run.dataset
		assert [:] == run.simulation
	}

	void testCreate() {
		thrown(RuntimeException, "Property 'id' is required") {
			service.create()
		}

		thrown(RuntimeException, "Property 'name' is required") {
			service.create(id: 'test')
		}

		thrown(RuntimeException, "Property 'dataset' is required") {
			service.create(id: 'test', name: 'Test Run')
		}

		thrown(RuntimeException, "Property 'simulation' is required") {
			service.create(id: 'test', name: 'Test Run', dataset: 'test-dataset')
		}

		thrown(RuntimeException, "Dataset 'test-dataset' does not exist") {
			service.create(id: 'test', name: 'Test Run', dataset: 'test-dataset', simulation: [:])
		}

		datasetService.create(id: 'test-dataset', name: 'Test Dataset', url: 'http://example.com')

		def run = service.create(id: 'test', name: 'Test Run', dataset: 'test-dataset', simulation: [:])
		assert run
		assert 'test' == run.id
		assert 'Test Run' == run.name
		assert 'test-dataset' == run.dataset
		assert [:] == run.simulation
		assert 'new' == run.status
		assert run.created

		assert 1 == eventService.collection.findAll(type: 'runStarted').size()

		thrown(RuntimeException, "Id 'test' already exists") {
			service.create(id: 'test', name: 'Test Run', dataset: 'test-dataset', simulation: [:])
		}
	}

	void testGetProgress() {
		assert null == service.getProgress('test')

		datasetService.create(id: 'test-dataset', name: 'Test Dataset', url: 'http://example.com')
		service.create(id: 'test', name: 'Test Run', dataset: 'test-dataset', simulation: [:])
		assert [] == service.getProgress('test')

		service.progressCollection.add(dataset: 'test-dataset', run: 'test', time: 1l, temp: 1.26d, score: 12345d)
		service.progressCollection.add(dataset: 'test-dataset', run: 'test', time: 2l, temp: 1.00d, score: 10000d)

		def list = service.getProgress('test')
		assert list
		assert 2 == list.size()

		def progress = list[0]
		assert 'test-dataset' == progress.dataset
		assert 'test' == progress.run
		assert 1l == progress.time
		assert 1.26d == progress.temp
		assert 12345d == progress.score
	}

	void testCreateProgress() {
		thrown(RuntimeException, "Run 'test' does not exist") {
			service.createProgress('test')
		}

		datasetService.create(id: 'test-dataset', name: 'Test Dataset', url: 'http://example.com')
		service.create(id: 'test', name: 'Test Run', dataset: 'test-dataset', simulation: [:])

		thrown(RuntimeException, "Property 'time' is required") {
			service.createProgress('test')
		}

		thrown(RuntimeException, "Property 'iteration' is required") {
			service.createProgress('test', [time: 1])
		}

		thrown(RuntimeException, "Property 'temp' is required") {
			service.createProgress('test', [time: 1, iteration: 10000])
		}

		thrown(RuntimeException, "Property 'score' is required") {
			service.createProgress('test', [time: 1, temp: 10.5, iteration: 100000])
		}

		thrown(RuntimeException, "Property 'objective' is required") {
			service.createProgress('test', [time: 1, temp: 10.5, score: 12345.67, iteration: 10000])
		}

		def progress = service.createProgress('test', [time: 1, temp: 10.5, score: 12345.67, iteration: 10000, objective: 'matrix'])
		assert progress
		assert 'test' == progress.run
		assert 'test-dataset' == progress.dataset
		assert 1 == progress.time
		assert 10.5 == progress.temp
		assert 12345.67 == progress.score
		assert 10000 == progress.iteration
		assert 'matrix' == progress.objective

		def run = service.get('test')
		assert 'active' == run.status
		assert 12345.67 == run.score

		// don't create newBestScore events for the first run of a dataset
		assert 0 == eventService.collection.findAll(type: 'newBestScore').size()

		// create a second run
		service.create(id: 'test2', name: 'Test Run', dataset: 'test-dataset', simulation: [:], score: (200000 as double))
		service.createProgress('test2', [time: 1, temp: 10.5, score: 10000.00, iteration: 10000, objective: 'matrix'])
		assert 1 == eventService.collection.findAll(type: 'newBestScore').size()
	}

	private thrown(clazz, msg, closure) {
		try {
			closure.call()
			assert false
		} catch (e) {
			assert clazz == e.class
			assert msg == e.message
		}
	}
}
