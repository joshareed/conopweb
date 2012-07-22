package conopweb

class DatasetService {
	static final String DATASETS = 'datasets'

	def mongoService

	protected getCollection() {
		mongoService.getCollection(DATASETS, true)
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

		def dataset = collection.find(id: id.toLowerCase())
		if (dataset) {
			dataset.remove('_id')
		}
		dataset
	}

	def create(Map params = [:]) {
		// check for required parameters
		['id', 'name', 'url'].each { key ->
			if (params[key] == null || params[key] == '') {
				throw new RuntimeException("Property '$key' is required")
			}
		}

		// check that id is unique
		def existing = get(params.id)
		if (existing) {
			throw new RuntimeException("Id '${params.id}' already exists")
		}

		// create the dataset
		def dataset = collection.add(id: params.id.toLowerCase(), name: params.name, url: params.url)
		if (dataset) {
			dataset.remove('_id')
		}
		dataset
	}
}
