class UrlMappings {

	static mappings = {
		"/api/1/datasets" {
			controller = "datasetApi"
			action = [GET: 'list', POST: 'create', PUT: 'create']
		}
		"/api/1/datasets/$id" {
			controller = "datasetApi"
			action = "show"
		}
		"/api/1/runs" {
			controller = "runApi"
			action = [GET: 'list', POST: 'create', PUT: 'create']
		}
		"/api/1/runs/$id" {
			controller = "runApi"
			action = "show"
		}
		"/api/1/runs/$id/progress" {
			controller = "runApi"
			action = [GET: 'listProgress', POST: 'createProgress', PUT: 'createProgress']
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
