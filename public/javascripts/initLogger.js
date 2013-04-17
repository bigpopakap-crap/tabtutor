//Define the Javascript logger
(function() {
	
	//define the Logger object type
	//This can be moved to it's own file later if necessary, but for now it's only used here
	function Logger() {
		
		//Public constants for each object
		this.Level = {
			NONE: { pri: -1000, name: 'NONE' },
			TRACE: { pri: 1, name: 'TRACE' },
			DEBUG: { pri: 2, name: 'DEBUG' },
			INFO: { pri: 3, name: 'INFO' },
			WARN: { pri: 4, name: 'WARN' },
			ERROR: { pri: 5, name: 'ERROR' }
		};
		
		//Private constants for each object
		var DEFAULT_PRODCUTION_LOG_LEVEL = this.Level.NONE;
		var DEFAULT_NON_PRODUCTION_LOG_LEVEL = this.Level.DEBUG;
		
		//Member variables
		var isInitialized = false;
		var curLevel = null;		//can only be set during initialization
		var allowAlerts = null;
		
		//Creates a function to take a level and message, and either logs it or alerts it,
		//if the logger level allows it and alerts are allowed
		var createLogOrAlertFn = function (isAlert) {
			return function (level, message) {
				//test whether we can print the message based on configuration of the logger
				if (isInitialized && curLevel.pri >= 0 && level.pri >= curLevel.pri) {
					var output = 'Logger [' + level.name + ']: ' + message;
					
					//either log or alert the message
					if (isAlert && allowAlerts) alert(output);
					else console.log(output + (isAlert ? ' (alert suppressed)' : ''));
				}
			}
		};
		
		//Initialization method
		this.init = function(isProduction) {
			if (!this.isInitialized) {
				curLevel = isProduction ? DEFAULT_PRODCUTION_LOG_LEVEL : DEFAULT_NON_PRODUCTION_LOG_LEVEL;
				allowAlerts = !isProduction;
				isInitialized = true;
				
				//Log that this has been initialized
				this.info('Logger initialized at level ' + this.getLevel().name);
			}
			else {
				//this has already been initialized, log an error
				this.warn('Logger has already been initialized');
			}
		};
		
		//Gets the current logging level
		this.getLevel = function() {
			if (isInitialized) {
				return curLevel;
			}
			else {
				//log an error
				console.log('Error: this Logger has not yet been initialized');
				return null;
			}
		};

		//Methods to log or alert messages
		this.log = createLogOrAlertFn(false);
		this.alert = createLogOrAlertFn(true);
		
		//Shortcut methods for logging at specific levels
		this.trace = function (message) { this.log(this.Level.TRACE, message); };
		this.debug = function (message) { this.log(this.Level.DEBUG, message); };
		this.info = function (message) { this.log(this.Level.INFO, message); };
		this.warn = function (message) { this.log(this.Level.WARN, message); };
		this.error = function (message) { this.log(this.Level.ERROR, message); };
		
		//Shortcut methods for alerting at specific levels
		this.debugAlert = function (message) { this.alert(this.Level.TRACE, message); };
		this.debugAlert = function (message) { this.alert(this.Level.DEBUG, message); };
		this.infoAlert = function (message) { this.alert(this.Level.INFO, message); };
		this.warnAlert = function (message) { this.alert(this.Level.WARN, message); };
		this.errorAlert = function (message) { this.alert(this.Level.ERROR, message); };
	};
	
	//define the logger in the namespace
	window.WTF.set('Logger', new Logger());
	
})();