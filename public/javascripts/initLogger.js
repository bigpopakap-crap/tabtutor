//Define the Javascript logger
(function() {
	
	//Defines the logging levels
	var _Level = {
		DEBUG: { pri: 1, name: 'DEBUG' },
		INFO: { pri: 2, name: 'INFO' },
		WARNING: { pri: 3, name: 'WARNING' },
		SEVERE: { pri: 4, name: 'SEVERE' },
		NONE: { pri: -1000, name: 'NONE' }
	};
	
	//Creates a function to take a level and message, and either logs it or alerts it,
	//if the logger level allows it and alerts are allowed
	var createLogOrAlertFn = function (isAlert) {
		return function (level, message) {
			//test whether we can print the message based on configuration of the logger
			if (this.curLevel.pri >= 0 && (!isAlert || this.allowAlerts) && level.pri >= this.curLevel.pri) {
				var output = 'Logger [' + level.name + ']: ' + message;
				
				//either log or alert the message
				if (isAlert) alert(output);
				else console.log(output);
			}
		}
	};

	window.WTF.initLogger = function (isProduction) {
		window.WTF.Logger = {
				//configure the Logger
				Level: _Level,
				curLevel: isProduction ? _Level.NONE : _Level.DEBUG,
				allowAlerts: !isProduction,
				
				//define functions to log or alert messages
				log: createLogOrAlertFn(false),
				alert: createLogOrAlertFn(true),
				
				//define shortcut methods for logging at specific levels
				debug: function (message) { this.log(_Level.DEBUG, message); },
				info: function (message) { this.log(_Level.INFO, message); },
				warning: function (message) { this.log(_Level.WARNING, message); },
				sever: function (message) { this.log(_Level.SEVERE, message); }
		};
		
		window.WTF.Logger.info("Logger initialized, set to level " + window.WTF.Logger.curLevel.name);
	}
	
})();