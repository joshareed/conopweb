package conopweb.api

import conopweb.*

import grails.test.mixin.*
import org.junit.*

@TestFor(DatasetApiController)
class DatasetApiControllerTests {
	def mongoService, datasetService, eventService

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

		controller.datasetService = datasetService
	}

	@After
	void teardown() {
		datasetService.collection.remove([:])
		eventService.collection.remove([:])
	}

	void testList() {
		controller.list()
		assert '[]' == response.contentAsString.toString()

		datasetService.collection.add(name: 'Test Dataset', url: 'http://example.com', id: 'test')

		response.reset()
		controller.list()

		assert '[{"name":"Test Dataset","url":"http://example.com","id":"test"}]' == response.contentAsString.toString()
	}

	void testShow() {
		controller.show()
		assert 404 == response.status
		assert "Dataset 'null' does not exist" == response.contentAsString.toString()

		response.reset()

		controller.params.id = 'test'
		controller.show()
		assert 404 == response.status
		assert "Dataset 'test' does not exist" == response.contentAsString.toString()

		response.reset()

		datasetService.collection.add(name: 'Test Dataset', url: 'http://example.com', id: 'test')
		controller.params.id = 'test'
		controller.show()
		assert '{"name":"Test Dataset","url":"http://example.com","id":"test"}' == response.contentAsString.toString()
	}

	void testCreate() {
		controller.create()
		assert 400 == response.status
		assert "Property 'id' is required" == response.contentAsString.toString()

		response.reset()

		controller.params.putAll(name: 'Test Dataset', url: 'http://example.com', id: 'test')
		controller.create()
		assert '{"id":"test","name":"Test Dataset","url":"http://example.com"}' == response.contentAsString.toString()
	}

	void testCreateWithJSON() {
		controller.request.contentType = "text/json"
		controller.request.content = '{"id":"test","name":"Test Dataset","url":"http://example.com"}'.getBytes()

		controller.create()
		assert '{"id":"test","name":"Test Dataset","url":"http://example.com"}' == response.contentAsString.toString()

		response.reset()

		controller.request.contentType = "text/json"
		controller.request.content = '{"id":"test","name":"Test Dataset",'.getBytes()

		controller.create()
		assert 400 == response.status
		assert "Invalid JSON" == response.contentAsString.toString()
	}
}
