var popupStatus = false;


	function showPopup() {
		if (popupStatus == false) {
			$('#popup').css({'opacity': '0.7'});
			$('#popup').fadeIn('slow');
			$('#popup_content').fadeIn('slow');
			popupStatus = true;
		}
	}

	function hidePopup() {
		if (popupStatus == true) {
			$('#popup').fadeOut('slow');
			$('#popup_content').fadeOut('slow');
			popupStatus = false;
		}
	}

	function centerPopup() {
		var windowWidth = document.documentElement.clientWidth;
		var windowHeight = document.documentElement.clientHeight;
		var popupHeight = $('#popup_content').height();
		var popupWidth = $('#popup_content').width();
		$('#popup_content').css({'top': windowHeight / 2 - popupHeight / 2, 'left': windowWidth / 2 - popupWidth / 2});
		$('#popup').css({'height': windowHeight});
	}
	
	//TODO: @poljpocket
	function getData(){
		var isOverlapping = false;
		$.getJSON('/calendars/overlaps/'+$('#startDate').val()+'-'+$('#startTime').val() + '/'+$('#endDate').val()+'-'+$('#endTime').val(),
				function(data){
					var items = [];
					  $.each(data, function(key, val) {
					    items.push(val);
					  });
					  
					  isOverlapping = items.length != 0;
				}
		);
	}
	
	$(document).ready(function (){
		centerPopup();

		$('#saveForm').submit(function() { 
			showPopup();
			return false;
	
		});
		
		$('#popup').click(function() {
			hidePopup();
		});
		
		$('#popup_content input').click(function() {
			hidePopup();
		});	
	});