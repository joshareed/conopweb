package conopweb.api

import grails.converters.JSON

class DatasetApiController {
	def datasetService

	def list() {
		params.remove('controller')
		params.remove('action')

		render datasetService.find(params) as JSON
	}

	def create() {
		if (request.contentLength > 0) {
			try {
				render datasetService.create(request.JSON) as JSON
			} catch (e) {
				render(status: 400, text: 'Invalid JSON')
			}
		} else {
			try {
				render datasetService.create(params) as JSON
			} catch (e) {
				render(status: 400, text: e.message)
			}
		}
	}

	def show(String id) {
		def dataset = datasetService.get(id)
		if (dataset) {
			render dataset as JSON
		} else {
			render(status: 404, text: "Dataset '$id' does not exist")
		}
	}
}
