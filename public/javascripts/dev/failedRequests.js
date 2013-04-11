(function () {
	
	//TODO detect for new elements that are added
	
	//What to do with failed requests
	function handleFailedRequest(type, url) {
		var message = type + ' request failed to URL ' + url;
		
		//Alert if it's a request to this site
		if (url.indexOf('http') == 0) {
			//This is to another website
			window.WTF.get('Logger').error(message);
		}
		else {
			window.WTF.get('Logger').errorAlert(message);
		}
	};
	
	//TODO atach these handlers before the requests are even sent out
	$(document).ready(function() {
		//Detect failed scripts
		$('script').error(function() {
			handleFailedRequest('Script', $(this).attr('src'));
		});
		
		//Detect failed stylesheets
		$('link[rel=stylesheet]').error(function() {
			handleFailedRequest('Stylesheet', $(this).attr('href'));
		});
		
		//Detect failed images
		$('img').error(function() {
			handleFailedRequest('Image', $(this).attr('src'));
		});
	});
	
	//Detect failed AJAX requests
	$(document).ajaxError(function(event, req, settings) {
		handleFailedRequest('AJAX', settings.url);
	});
	
})();