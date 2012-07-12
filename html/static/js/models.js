/***
 * API
 ***/
var API = {
	root: window.API_ROOT, 
	login: window.API_LOGIN,
	key: window.API_KEY,
	get: function(params) {
		params['type'] = 'GET';
		API.request(params);
	}, 
	request: function(params) {
		API.trace(params['type'] + ' : ' + API_ROOT + params['url']);
		$.ajax({
			url: API_ROOT + params['url'] + '?format=jsonp&username=' + API.login + '&api_key=' + API.key, 
			type: params['type'], 
			dataType: 'jsonp',
			success: function(data) { 
				API.trace(data);
				params['success'].call(null, data); 
			}, error: function(error) { 
				API.trace(error);
				params['error'].apply(null, data); }
		});
	}, 
	trace: function(msg) {
		console.log(msg);
	}
}

/***
 * MODELS
 ***/

window.Checkin = Backbone.Model.extend({
	url: API_ROOT + 'checkin/',
	defaults: {
		place: false,
		user: false,
		level: 0,
		status: '', 
		added: false
	}
}); 

window.Feed = Backbone.Collection.extend({
    model:Checkin,
    url: API_ROOT + 'feed/'
});

/*
var Checkin = {
	renderListItem: function(item, container) {
		//container = $(container);
		console.log(container);
		var li = $('<li></li>').addClass('checkin');
		var a = $('<a></a>').html(item.status)
							.attr('href', '#checkin')
							.appendTo(li);		
		container.append(li);
		//container.listview('refresh');
	}
}*/