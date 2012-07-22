package conopweb

class IndexController {
	def eventService, datasetService, runService

	def index() {
		[events: eventService.recent(),
			datasets: datasetService.find(limit: 10),
			runs: runService.find(sort: '-created', limit: 10),
			active: runService.find(status: 'active', limit: 10)]
	}
}
