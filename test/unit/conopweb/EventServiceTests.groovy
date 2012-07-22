package conopweb

import grails.test.mixin.*
import org.junit.*

@TestFor(EventService)
class EventServiceTests {
	def mongoService

	@Before
	void setup() {
		mongoService = new MongoService('localhost', 'conopweb_test')

		service.mongoService = mongoService
		service.collection.remove([:])
	}

	@After
	void teardown() {
		service.collection.remove([:])
	}

	void testRunStarted() {
		def event = service.runStarted(id: 'test-run', dataset: 'test-dataset')
		assert event
		assert 'runStarted' == event.type
		assert 'test-run' == event.run
		assert 'test-dataset' == event.dataset
	}

	void testRunCompleted() {
		def event = service.runCompleted(id: 'test-run', dataset: 'test-dataset', score: (10000 as double), time: (60 as int))
		assert event
		assert 'runCompleted' == event.type
		assert 'test-run' == event.run
		assert 'test-dataset' == event.dataset
		assert 10000 == event.score
		assert 60 == event.time
	}

	void testDatasetCreated() {
		def event = service.datasetCreated(id: 'test-dataset')
		assert event
		assert 'datasetCreated' == event.type
		assert 'test-dataset' == event.dataset
	}

	void testNewBestScore() {
		def event = service.newBestScore(id: 'test-run', dataset: 'test-dataset', score: (10000 as double))
		assert event
		assert 'newBestScore' == event.type
		assert 'test-run' == event.run
		assert 'test-dataset' == event.dataset
		assert 10000 == event.score
	}

	void testRecent() {
		service.datasetCreated(id: 'test-dataset')
		service.runStarted(id: 'test-run', dataset: 'test-dataset')
		service.runCompleted(id: 'test-run', dataset: 'test-dataset', score: (10000 as double), time: (60 as int))
		service.newBestScore(id: 'test-run', dataset: 'test-dataset', score: (10000 as double))

		def recent = service.recent()
		assert 4 == recent.size()
	}
}
