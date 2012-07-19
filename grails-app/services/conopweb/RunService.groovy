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
		// figure out sort
		def sort = [:]
		if (params.sort) {
			def s = params.remove('sort')
			if (s.startsWith('-')) {
				sort[s.substring(1)] = -1
			} else {
				sort[s] = 1
			}
		} else {
			sort = ['created' : 1]
		}

		// figure out limit
		int limit = 0
		if (params.limit) {
			limit = params.remove('limit') as int
		}

		def query = [:]
		query.putAll(params)

		collection.findAll(query).sort(sort).limit(limit).collect { it.remove('_id'); it }
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
		def run = collection.add(id: params.id.toLowerCase(), name: params.name, dataset: dataset.id, simulation: params.simulation, created: new Date(), status: 'new')
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

		def query = [run: run.id]
		query.putAll(params)

		progressCollection.findAll(query).sort(time: 1).collect { it.remove('_id'); it }
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
		def progress = progressCollection.add(run: run.id, dataset: run.dataset, time: (params.time as int), temp: (params.temp as double),
			score: (params.score as double), objective: params.objective, iteration: (params.iteration as long))
		if (progress) {
			progress.remove('_id')
		}

		// update the run
		collection.update([id: run.id], ['$set': [score: progress.score, status: 'active', elapsed: progress.time]])
		if (params.solution && !params.solution.score) {
			collection.update([id: run.id], ['$set': [solution: params.solution]])
		}

		progress
	}

	def getSolution(String id, Map params = [:]) {
		// make sure run exists
		def run = get(id)
		if (!run) {
			throw new RuntimeException("Run '${id}' does not exist")
		}

		return run?.solution;
	}

	def createSolution(String id, Map params = [:]) {
		// make sure run exists
		def run = get(id)
		if (!run) {
			throw new RuntimeException("Run '${id}' does not exist")
		}

		if (params.score) {
			collection.update([id: run.id], ['$set': [solution: params, status: 'complete']])
		} else {
			collection.update([id: run.id], ['$set': [solution: null, status: 'aborted']])
		}
	}
}
