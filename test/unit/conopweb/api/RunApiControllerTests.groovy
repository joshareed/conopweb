package conopweb.api

import conopweb.*

import grails.test.mixin.*
import org.junit.*

import grails.converters.JSON

@TestFor(RunApiController)
class RunApiControllerTests {
	def mongoService, runService, datasetService

	@Before
	void setup() {
		mongoService = new MongoService('localhost', 'conopweb_test')

		datasetService = new DatasetService()
		datasetService.mongoService = mongoService
		datasetService.collection.remove([:])

		runService = new RunService()
		runService.mongoService = mongoService
		runService.datasetService = datasetService
		runService.collection.remove([:])

		controller.runService = runService
	}

	@After
	void teardown() {
		runService.collection.remove([:])
		runService.progressCollection.remove([:])
		datasetService.collection.remove([:])
	}

	void testList() {
		controller.list()
		assert '[]' == response.contentAsString

		runService.collection.add(id: 'test', name: 'Test Run', dataset: 'dataset', simulation: [:])

		response.reset()
		controller.list()

		assert '[{"id":"test","name":"Test Run","dataset":"dataset","simulation":{}}]' == response.contentAsString.toString()
	}

	void testCreate() {
		controller.create()
		assert 400 == response.status
		assert "Property 'id' is required" == response.contentAsString.toString()

		response.reset()

		controller.params.putAll(id: 'test', name: 'Test Run', dataset: 'test-dataset', simulation: [:])
		controller.create()
		assert 400 == response.status
		assert "Dataset 'test-dataset' does not exist"

		response.reset()

		datasetService.create(id: 'test-dataset', name: 'Test Dataset', url: 'http://example.com')
		controller.params.putAll(id: 'test', name: 'Test Run', dataset: 'test-dataset', simulation: [:])
		controller.create()
		def out = response.contentAsString.toString()
		assert out.startsWith('{')
		assert out.endsWith('}')
		assert out.contains('"id":"test"')
		assert out.contains('"name":"Test Run"')
		assert out.contains('"dataset":"test-dataset"')
		assert out.contains('"simulation":{}')
		assert out.contains('"status":"new"')
		assert out.contains('"created":')
	}

	void testShow() {
		controller.show()
		assert 404 == response.status
		assert "Run 'null' does not exist" == response.contentAsString.toString()

		response.reset()

		controller.params.id = 'test'
		controller.show()
		assert 404 == response.status
		assert "Run 'test' does not exist" == response.contentAsString.toString()

		response.reset()

		datasetService.create(id: 'test-dataset', name: 'Test Dataset', url: 'http://example.com')
		runService.create(id: 'test', name: 'Test Run', dataset: 'test-dataset', simulation: [:])

		controller.params.id = 'test'
		controller.show()
		def out = response.contentAsString.toString()
		assert out.startsWith('{')
		assert out.endsWith('}')
		assert out.contains('"id":"test"')
		assert out.contains('"name":"Test Run"')
		assert out.contains('"dataset":"test-dataset"')
		assert out.contains('"simulation":{}')
		assert out.contains('"status":"new"')
		assert out.contains('"created":')
	}

	void testListProgress() {
		controller.listProgress()
		assert 404 == response.status
		assert "Run 'null' does not exist" == response.contentAsString.toString()

		response.reset()

		datasetService.create(id: 'test-dataset', name: 'Test Dataset', url: 'http://example.com')
		runService.create(id: 'test', name: 'Test Run', dataset: 'test-dataset', simulation: [:])
		controller.params.id = 'test'
		controller.listProgress()
		assert '[]' == response.contentAsString.toString()
	}

	void testCreateProgress() {
		controller.params.id = 'test'
		controller.createProgress()
		assert 404 == response.status
		assert "Run 'test' does not exist" == response.contentAsString.toString()

		response.reset()

		datasetService.create(id: 'test-dataset', name: 'Test Dataset', url: 'http://example.com')
		runService.create(id: 'test', name: 'Test Run', dataset: 'test-dataset', simulation: [:])
		controller.params.putAll(time: '1', temp: '10.56', score: '12345.67', iteration: '10000', objective: 'matrix')
		controller.createProgress()

		assert '{"run":"test","dataset":"test-dataset","time":1,"temp":10.56,"score":12345.67,"objective":"matrix","iteration":10000}' == response.contentAsString.toString()
	}
}
