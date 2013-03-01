//Initialize the app's Javascript namespace
if (!window.WTF) {
	//This is a map of objects protected with a getter and setter
	//This can be moved to it's own file later if necessary, but for now it's only used here
	function Namespace() {
		
		//private function to use the logger or just directly print to the console
		function _log(message) {
//			if (this.get('Logger')) this.get('Logger').error(message);
//			else cosnsole.log('Error: ' + message);
		}
		
		//holds the actual values set
		var contents = {};
		
		//getter method
		this.get = function(key) {
			var retVar = contents[key] || null;
			if (!retVar) _log('Returned null value for key ' + key);
			return retVar;
		}
		
		//setter method, doesn't overwrite existing values
		this.set = function(key, val) {
			if (this.get(key)) {
				//log some error and don't set the value
				_log('Tried to overwrite existing object in global namespace');
			}
			else {
				//set the value
				contents[key] = val;
			}
		}
		
	};
	
	//initialize the namespace
	window.WTF = new Namespace();
}
else {
	//the Logger should have been initialized by now since the namespace already exists
	window.WTF.get('Logger').warn('Global namespace already initialized');
}