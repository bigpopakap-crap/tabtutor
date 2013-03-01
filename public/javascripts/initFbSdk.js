//Initialize the Facebook SDK and redirect to login if user is connected
(function() {
	
	/*
	 * appId - the Facebook app ID for the site
	 * redirectIfConnected - true if the site should automatically log the user in if they're connected
	 * loginUrl - the URL to redirect to if the user is connect through Facebook
	 */
	window.WTF.set('initFbSdk', function (appId, redirectIfConnected, loginUrl) {
		//Initialize the FB object after it is loaded
		FB.init({
			appId      : appId,
			channelUrl : null,
			status     : true,
			cookie     : false,
			xfbml      : false
		});
		
		if (redirectIfConnected) {
			// If logged in, redirect to the Facebook login handler
			FB.getLoginStatus(function (response) {
				if (response.status === 'connected') {
					window.location = loginUrl;
				}
			});
		}
	});
	
})();