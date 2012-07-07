package conopweb.api

class RunApiController {

	def list() {
		render 'list'
	}

	def create() {
		render 'create'
	}

	def show(String id) {
		render "Show $id"
	}

	def listProgress(String id) {
		render "List progress for $id"
	}

	def createProgress(String id) {
		render "Create progress fo $id"
	}

	// Run
	/*
	[
		id: '<run id>',
		name: 'Run Name',
		dataset: '<dataset id>',
		simulation: [
			key: 'value'
		],
		solution: [
			[
				name: 'Event LAD',
				rank: [
					min: 0,
					max: 1,
					rank: 1
				],
				sections: [
					'Section Name': [
						observed: -1,
						placed: -1
					]
				]
			]
		]
	]
	*/


	// Progress
	/*
	[
		dataset: '<dataset id>',
		run: '<run id>',
		time: 1,
		temp: 1000,
		score: 30000
	]
	*/

}
