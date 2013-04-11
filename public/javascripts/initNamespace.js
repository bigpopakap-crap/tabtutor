(function() {
	
	//Initialize the app's Javascript namespace
	if (!window.WTF) {
		//This is a map of objects protected with a getter and setter
		//This can be moved to it's own file later if necessary, but for now it's only used here
		function Namespace() {
			
			//private function to use the logger or just directly print to the console
			function _log(message, useAlert) {
				if (this.get('Logger') && useAlert) this.get('Logger').errorAlert(message);
				else if (this.get('Logger')) this.get('Logger').error(message);
				else if (useAlert) alert('Error: ' + message);
				else cosnsole.log('Error: ' + message);
			};
			
			//private function to get a value, used so that internal calls won't infinite loop
			function _get(key, shouldLog) {
				var retVar = contents[key] || null;
				if (shouldLog && !retVar) _log.call(this, 'Returned null value for key ' + key);
				return retVar;
			};
			
			//holds the actual values set
			var contents = {};
			
			//getter method
			this.get = function(key) {
				return _get(key, true);
			};
			
			//setter method, doesn't overwrite existing values
			this.set = function(key, val) {
				if (_get(key, false)) {
					//log some error and don't set the value
					_log.call(this, 'Tried to overwrite existing object in global namespace: ' + key, true);
				}
				else {
					//set the value
					contents[key] = val;
				}
			};
			
		};
		
		//initialize the namespace
		window.WTF = new Namespace();
	}
	else {
		//the Logger should have been initialized by now since the namespace already exists
		window.WTF.get('Logger').warn('Global namespace already initialized');
	}
	
})();