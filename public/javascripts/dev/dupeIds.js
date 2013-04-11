(function() {
	$(document).ready(function () {
		
		//Go through each element with an ID
		$('[id]').each(function() {
			var dupeIds = $('[id=\'' + this.id + '\']');
			
			//If there are more than one, and this is the first (to stop multiple log lines), log an error
			if (dupeIds.length > 1 && dupeIds[0] == this) {
				window.WTF.get('Logger').errorAlert(
					'Duplicated IDs detected in DOM: ' + this.id + ' (' + dupeIds.length + ' instances found)'
				);
			}
		});
		
	});
})();