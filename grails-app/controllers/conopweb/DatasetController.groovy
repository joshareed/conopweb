package conopweb

class DatasetController {
	static defaultAction = 'list'

	def datasetService, runService

    def list() {
		[datasets: datasetService.find(params)]
	}

	def show(String id) {
		def dataset = datasetService.get(id)
		if (!dataset) {
			render(status: 404, text: "Dataset '$id' does not exist")
		} else {
			[
				dataset: dataset,
				recent: runService.find(dataset: dataset.id, sort: '-created', limit: 5),
				best: runService.find(dataset: dataset.id, sort: 'score', limit: 5)
			]
		}
	}
}
