package conopweb

class IndexController {
	def eventService, datasetService, runService

	def index() {
		[events: eventService.recent(),
			datasets: datasetService.find(limit: 10, sort: '-ts'),
			runs: runService.find(limit: 10, sort: '-created'),
			active: runService.find(status: 'active', limit: 10, sort: '-created')]
	}
}
