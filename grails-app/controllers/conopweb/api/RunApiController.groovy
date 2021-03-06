package conopweb.api

import grails.converters.JSON

class RunApiController {
	def runService

	def list() {
		params.remove('controller')
		params.remove('action')

		render runService.find(params) as JSON
	}

	def show(String id) {
		def run = runService.get(id)
		if (run) {
			render run as JSON
		} else {
			render(status: 404, text: "Run '$id' does not exist")
		}
	}

	def create() {
		if (request.contentLength > 0) {
			try {
				render runService.create(request.JSON) as JSON
			} catch (e) {
				render(status: 400, text: 'Invalid JSON')
			}
		} else {
			try {
				render runService.create(params) as JSON
			} catch (e) {
				render(status: 400, text: e.message)
			}
		}
	}

	def listProgress(String id) {
		def progress = runService.getProgress(id, [:])
		if (progress == null) {
			render(status: 404, text: "Run '$id' does not exist")
		} else {
			render progress as JSON
		}
	}

	def createProgress(String id) {
		// check the run
		def run = runService.get(id)
		if (!run) {
			render(status: 404, text: "Run '$id' does not exist")
			return
		}

		// create the progress
		if (request.contentLength > 0) {
			try {
				render runService.createProgress(id, request.JSON) as JSON
			} catch (e) {
				render(status: 400, text: 'Invalid JSON')
			}
		} else {
			try {
				render runService.createProgress(id, params) as JSON
			} catch (e) {
				render(status: 400, text: e.message)
			}
		}
	}

	def getSolution(String id) {
		// check the run
		def run = runService.get(id)
		if (!run) {
			render(status: 404, text: "Run '$id' does not exist")
			return
		}

		def solution = run.solution ?: [:]
		render solution as JSON
	}

	def createSolution(String id) {
		// check the run
		def run = runService.get(id)
		if (!run) {
			render(status: 404, text: "Run '$id' does not exist")
			return
		}

		// create the solution
		if (request.contentLength > 0) {
			try {
				render runService.createSolution(id, request.JSON) as JSON
			} catch (e) {
				render(status: 400, text: 'Invalid JSON')
			}
		} else {
			try {
				render runService.createSolution(id, params) as JSON
			} catch (e) {
				render(status: 400, text: e.message)
			}
		}
	}
}
