package conopweb

import grails.test.mixin.*
import org.junit.*

@TestFor(DatasetService)
class DatasetServiceTests {
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

	void testFind() {
		assert [] == service.find()

		service.collection.add(name: 'Test Dataset', url: 'http://example.com', id: 'test')

		def list = service.find()
		assert list
		assert 1 == list.size()

		def dataset = list[0]
		assert 'Test Dataset' == dataset.name
		assert 'http://example.com' == dataset.url
		assert 'test' == dataset.id
	}

	void testGet() {
		assert null == service.get(null)
		assert null == service.get('')
		assert null == service.get('test')

		service.collection.add(name: 'Test Dataset', url: 'http://example.com', id: 'test')

		def dataset = service.get('test')
		assert dataset
		assert 'Test Dataset' == dataset.name
		assert 'http://example.com' == dataset.url
		assert 'test' == dataset.id
	}

	void testCreate() {
		thrown(RuntimeException, "Property 'id' is required") {
			service.create()
		}

		thrown(RuntimeException, "Property 'name' is required") {
			service.create(id: 'test')
		}

		thrown(RuntimeException, "Property 'url' is required") {
			service.create(id: 'test', name: 'Test Dataset')
		}

		def dataset = service.create(id: 'test', name: 'Test Dataset', url: 'http://example.com')
		assert dataset
		assert 'Test Dataset' == dataset.name
		assert 'http://example.com' == dataset.url
		assert 'test' == dataset.id

		thrown(RuntimeException, "Id 'test' already exists") {
			service.create(id: 'test', name: 'Test Dataset', url: 'http://example.com')
		}
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
