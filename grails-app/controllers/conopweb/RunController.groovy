package conopweb

class RunController {
	static defaultAction = 'list'

	def runService

	def list() {
		[runs: runService.find(params)]
	}

	def show(String id) {
		def run = runService.get(id)
		if (!run) {
			render(status: 404, text: "Run '$id' does not exist")
		} else {
			[run: run]
		}
	}
}
