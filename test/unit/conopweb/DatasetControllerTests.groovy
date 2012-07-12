package conopweb

import grails.test.mixin.*
import org.junit.*

@TestFor(DatasetController)
class DatasetControllerTests {

	@Before
	void setup() {
		controller.datasetService = [
			find: { Map params -> ['dataset'] },
			get: { def id -> id == 'test' ? 'test' : null }
		] as DatasetService
	}

	void testList() {
		def out = controller.list()

		assert out
		assert 1 == out.size()
		assert out.containsKey('datasets')
	}

	void testShow() {
		controller.show()
		assert 404 == response.status

		response.reset()

		controller.params.id = 'test'
		def out = controller.show()
		assert 404 != response.status

		assert out
		assert 1 == out.size()
		assert out.containsKey('dataset')
	}
}
