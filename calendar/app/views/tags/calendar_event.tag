<table id="event_event">
	<tr>
		<td class="event_title">${_event.name} <a href="@{Calendars.editEvent(_calendar.id, _event.id)}"><img src="/public/images/edit.jpeg" /></a> <a href="@{Calendars.deleteEvent(_event.id)}"><img src="/public/images/delete.jpeg" /></a></td>
		<td class="event_properties">(#{if isPublic}public)#{/if}#{else}private)#{/else}</td>
	</tr>
	<tr>
		<td class="event_date" colspan="2">${_event.start.format('yyyy-MM-dd, HH:mm')} - ${_event.end.format('yyyy-MM-dd, HH:mm')}</td>
	</tr>
	<tr>
		<td class="event_note" colspan="2">Note: ${_event.note}</td>
	</tr>
</table>