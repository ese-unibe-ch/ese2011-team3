var openPopups = 0;

function showPopup(name) {
	openPopups++;
    centerPopup(name);
    var popup_background = '#popup_background[name=' + name +']';
    var popup_content = '#popup_content[name=' + name +']';
    $(popup_background).css({'z-index': (openPopups * 2 - 1), opacity: '0.0', display: 'block'});
	$(popup_content).css({'z-index': (openPopups * 2), opacity: '0.0', display: 'block'});
    $(popup_background).animate({opacity: '0.3'}, 400);
	$(popup_content).animate({opacity: '1.0'}, 200);
}

function hidePopup(name) {
    var popup_background = '#popup_background[name=' + name +"" +']';
    var popup_content = '#popup_content[name=' + name +"" +']';
    $(popup_content).animate({opacity: '0.0'}, 200);
	$(popup_background).animate({opacity: '0.0'}, 400, function() {
    	$(popup_background).css({opacity: '1.0', display: 'none'});
		$(popup_content).css({opacity: '1.0', display: 'none'});
	});
	openPopups--;
}

function centerPopup(name) {
    var popup_background = '#popup_background[name=' + name +"" +']';
    var popup_content = '#popup_content[name=' + name +"" +']';
    var windowWidth = document.documentElement.clientWidth;
    var windowHeight = document.documentElement.clientHeight;
    var popupHeight = $(popup_content).height();
    var popupWidth = $(popup_content).width();
    $(popup_content).css({'top': windowHeight / 2 - popupHeight / 2, 'left': windowWidth / 2 - popupWidth / 2});
    $(popup_background).css({'height': windowHeight});
}