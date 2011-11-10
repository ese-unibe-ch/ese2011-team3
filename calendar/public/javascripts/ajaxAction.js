function AjaxAction(action, type) {
	this.action = action;
	this.type = type;
	
	this.perform = function(options, callback) {
		$.ajax(
			{
				url: this.action(options),
				dataType: this.type, 
				success: function(data) {
					callback(data);
				}
			}
		);
	};
}