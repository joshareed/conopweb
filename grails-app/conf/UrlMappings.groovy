class UrlMappings {

	static mappings = {
		"/api/datasets" {
			controller = "datasetApi"
			action = [GET: 'list', POST: 'create', PUT: 'create']
		}
		"/api/datasets/$id" {
			controller = "datasetApi"
			action = "show"
		}
		"/api/runs" {
			controller = "runApi"
			action = [GET: 'list', POST: 'create', PUT: 'create']
		}
		"/api/runs/$id" {
			controller = "runApi"
			action = "show"
		}
		"/api/runs/$id/progress" {
			controller = "runApi"
			action = [GET: 'listProgress', POST: 'createProgress', PUT: 'createProgress']
		}
		"/api/runs/$id/solution" {
			controller = "runApi"
			action = [GET: 'getSolution', POST: 'createSolution', PUT: 'createSolution']
		}
		"/datasets"(controller: 'dataset', action: 'list')
		"/datasets/$id"(controller: 'dataset', action: 'show')
		"/runs"(controller: 'run', action: 'list')
		"/runs/$id"(controller: 'run', action: 'show')

		"/"(controller: 'index')
		"500"(view:'/error')
	}
}
