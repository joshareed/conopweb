package conopweb

class EventService {
	static final String EVENTS = 'events'

	def mongoService

	protected getCollection() {
		mongoService.getCollection(EVENTS, true)
	}

	def runStarted(Map run) {
		def event = collection.add(type: 'runStarted', run: run.id, runName: run.name, dataset: run.dataset, ts: new Date())
		event.remove('_id')
		event
	}

	def runCompleted(Map run) {
		def event = collection.add(type: 'runCompleted', run: run.id, runName: run.name, dataset: run.dataset, score: run.score, time: run.time, ts: new Date())
		event.remove('_id')
		event
	}

	def datasetCreated(Map dataset) {
		def event = collection.add(type: 'datasetCreated', dataset: dataset.id, datasetName: dataset.name, ts: new Date())
		event.remove('_id')
		event
	}

	def newBestScore(Map run) {
		def event = collection.add(type: 'newBestScore', run: run.id, runName: run.name, dataset: run.dataset, score: run.score, ts: new Date())
		event.remove('_id')
		event
	}

	def recent() {
		collection.findAll([:]).sort([ts: -1]).limit(15).collect { it.remove('_id'); it }
	}
}
