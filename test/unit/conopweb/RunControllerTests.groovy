package conopweb

import grails.test.mixin.*
import org.junit.*

@TestFor(RunController)
class RunControllerTests {

	@Before
	void setup() {
		controller.runService = [
			find: { Map params -> [[id: 'test-dataset']] },
			get: { def id -> id == 'test' ? [id: 'test', dataset: 'test-dataset'] : null }
		] as RunService
	}

	void testList() {
		def out = controller.list()

		assert out
		assert 1 == out.size()
		assert out.containsKey('runs')
	}

	void testShow() {
		controller.show()
		assert 404 == response.status

		response.reset()

		controller.params.id = 'test'
		def out = controller.show()
		assert 404 != response.status

		assert out
		assert 3 == out.size()
		assert out.containsKey('run')
		assert out.containsKey('runCount')
		assert out.containsKey('rank')
	}
}
