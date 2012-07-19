package conopweb

class RunController {
	static defaultAction = 'list'

	def runService

	def list() {
		[runs: runService.find([:])]
	}

	def show(String id) {
		def run = runService.get(id)
		if (!run) {
			return render(status: 404, text: "Run '$id' does not exist")
		}

		def model = [run: run]

		// figure out our rank
		def runs = runService.find(dataset: run.dataset, sort: 'score').collect { it.id }
		model.runCount = runs.size()
		model.rank = runs.indexOf(run.id) + 1

		// add in last progress if active run
		if (run.status == 'active') {
			def list = runService.getProgress(run.id, [score: run.score])
			if (list.size() > 0) {
				model.lastProgress = list.last().time - list.first().time
			}
		}
		model
	}
}
