package conopweb

class DatasetService {
	static final String DATASETS = 'datasets'

	def mongoService

	protected getCollection() {
		mongoService.getCollection(DATASETS, true)
	}

    def find(Map params = [:]) {
		collection.findAll([:]).collect { it.remove('_id'); it }
    }

	def get(String id) {
		if (!id) { return null }

		def dataset = collection.find(id: id)
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
		def dataset = collection.add(id: params.id, name: params.name, url: params.url)
		if (dataset) {
			dataset.remove('_id')
		}
		dataset
	}
}
