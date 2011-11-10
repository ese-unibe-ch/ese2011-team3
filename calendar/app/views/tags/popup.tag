*{
	Displays a popup.
	
	@param: _name the name of the popup
}*

#{stylesheet 'popup.css' /}
#{script 'popup.js' /}

<div id="popup_background" name="${_name}"></div>

#{if _width && _height}
<div id="popup_content" name="${_name}" style="width: ${_width}px; height: ${_height}px;">
#{/if}
#{else}
<div id="popup_content" name="${_name}">
#{/else}
	#{doBody /}
</div>