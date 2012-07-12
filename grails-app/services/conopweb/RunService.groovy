package conopweb

class RunService {
	static final String RUNS = 'runs'
	static final String PROGRESS = 'progress'

	def mongoService, datasetService

	protected getCollection() {
		mongoService.getCollection(RUNS, true)
	}

	protected getProgressCollection() {
		mongoService.getCollection(PROGRESS, true)
	}

    def find(Map params = [:]) {
		collection.findAll([:]).collect { it.remove('_id'); it }
    }

	def get(String id) {
		if (!id) { return null }

		def run = collection.find(id: id.toLowerCase())
		if (run) {
			run.remove('_id')
		}
		run
	}

	def create(Map params = [:]) {
		// check for required parameters
		['id', 'name', 'dataset', 'simulation'].each { key ->
			if (params[key] == null || params[key] == '') {
				throw new RuntimeException("Property '$key' is required")
			}
		}

		// check that id is unique
		def existing = get(params.id)
		if (existing) {
			throw new RuntimeException("Id '${params.id}' already exists")
		}

		// check that dataset exists
		def dataset = datasetService.get(params.dataset)
		if (!dataset) {
			throw new RuntimeException("Dataset '${params.dataset}' does not exist")
		}

		// create the run
		def run = collection.add(id: params.id.toLowerCase(), name: params.name, dataset: params.dataset, simulation: params.simulation)
		if (run) {
			run.remove('_id')
		}
		run
	}

	def getProgress(String id, Map params = [:]) {
		def run = get(id)
		if (!run) {
			return null
		}

		progressCollection.findAll(run: id).collect { it.remove('_id'); it }
	}

	def createProgress(String id, Map params = [:]) {
		// make sure run exists
		def run = get(id)
		if (!run) {
			throw new RuntimeException("Run '${id}' does not exist")
		}

		// check for required parameters
		['time', 'iteration', 'temp', 'score', 'objective'].each { key ->
			if (params[key] == null || params[key] == '') {
				throw new RuntimeException("Property '$key' is required")
			}
		}

		// create the progress
		def progress = progressCollection.add(run: id, dataset: run.dataset, time: (params.time as int), temp: (params.temp as double),
			score: (params.score as double), objective: params.objective, iteration: (params.iteration as long))
		if (progress) {
			progress.remove('_id')
		}
		progress
	}
}
