package conopweb

import grails.test.mixin.*
import org.junit.*

@TestFor(IndexController)
class IndexControllerTests {
	def mongoService, runService, datasetService, eventService

	@Before
	void setup() {
		mongoService = new MongoService('localhost', 'conopweb_test')

		eventService = new EventService()
		eventService.mongoService = mongoService
		eventService.collection.remove([:])

		datasetService = new DatasetService()
		datasetService.mongoService = mongoService
		datasetService.eventService = eventService
		datasetService.collection.remove([:])

		runService = new RunService()
		runService.mongoService = mongoService
		runService.datasetService = datasetService
		runService.eventService = eventService
		runService.collection.remove([:])

		controller.eventService = eventService
		controller.datasetService = datasetService
		controller.runService = runService
	}

	@After
	void teardown() {
		runService.collection.remove([:])
		runService.progressCollection.remove([:])
		datasetService.collection.remove([:])
		eventService.collection.remove([:])
	}

	void testIndex() {
		def out = controller.index()
		assert 4 == out.size()
		assert out.containsKey('events')
		assert out.containsKey('datasets')
		assert out.containsKey('runs')
		assert out.containsKey('active')
	}
}
