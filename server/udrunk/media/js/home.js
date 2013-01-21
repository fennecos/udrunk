$(document).ready(function() {
	data = API.get({
		url: 'feed/', 
		success: function(data) {
			alert(data);
		}, 
		error: function(data) {

		}
	});
});