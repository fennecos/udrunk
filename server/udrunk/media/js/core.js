var API = {
	root: window.API_ROOT, 
	get: function(params) {
		params['type'] = 'GET';
		API.request(params);
	}, 
	request: function(params) {
		API.trace(params['type'] + ' : ' + API_ROOT + params['url']);
		$.ajax({
			url: API_ROOT + params['url'] + '?format=json', 
			type: params['type'], 
			success: function(data) { 
				API.trace(data);
				params['success'].call(data); },
			error: function(error) { 
				API.trace(error);
				params['error'].call(data); }
		});
	}, 
	trace: function(msg) {
		console.log(msg);
	}
}