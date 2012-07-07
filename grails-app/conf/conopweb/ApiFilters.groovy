package conopweb

class ApiFilters {

    def filters = {
        api(uri: '/api/**') {
            after = { Map model ->
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Methods"));
				response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
				response.setHeader("Access-Control-Max-Age", "86400");
            }
        }
    }
}
