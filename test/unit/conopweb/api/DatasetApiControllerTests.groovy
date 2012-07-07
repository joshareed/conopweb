package conopweb.api

import conopweb.*

import grails.test.mixin.*
import org.junit.*

@TestFor(DatasetApiController)
class DatasetApiControllerTests {
	def mongoService, datasetService

	@Before
	void setup() {
		mongoService = new MongoService('localhost', 'conopweb_test')

		datasetService = new DatasetService()
		datasetService.mongoService = mongoService

		controller.datasetService = datasetService
	}

	@After
	void teardown() {
		datasetService.collection.remove([:])
	}

	void testList() {
		controller.list()
		assert '[]' == response.contentAsString

		datasetService.collection.add(name: 'Test Dataset', url: 'http://example.com', id: 'test')

		response.reset()
		controller.list()

		assert '[{"name":"Test Dataset","url":"http://example.com","id":"test"}]' == response.contentAsString
	}

	void testShow() {
		controller.show()
		assert 404 == response.status
		assert 'Dataset not found' == response.contentAsString

		response.reset()

		controller.params.id = 'test'
		controller.show()
		assert 404 == response.status
		assert 'Dataset not found' == response.contentAsString

		response.reset()

		datasetService.collection.add(name: 'Test Dataset', url: 'http://example.com', id: 'test')
		controller.params.id = 'test'
		controller.show()
		assert '{"name":"Test Dataset","url":"http://example.com","id":"test"}' == response.contentAsString
	}

	void testCreate() {
		controller.create()
		assert 400 == response.status
		assert "'id' is required" == response.contentAsString

		response.reset()

		controller.params.putAll(name: 'Test Dataset', url: 'http://example.com', id: 'test')
		controller.create()
		assert '{"id":"test","name":"Test Dataset","url":"http://example.com"}' == response.contentAsString
	}

	void testCreateWithJSON() {
		controller.request.contentType = "text/json"
		controller.request.content = '{"id":"test","name":"Test Dataset","url":"http://example.com"}'.getBytes()

		controller.create()
		assert '{"id":"test","name":"Test Dataset","url":"http://example.com"}' == response.contentAsString

		response.reset()

		controller.request.contentType = "text/json"
		controller.request.content = '{"id":"test","name":"Test Dataset",'.getBytes()

		controller.create()
		assert 400 == response.status
		assert "Invalid JSON" == response.contentAsString
	}

}
