<td id="${_day.type.getId()}">
	<a href="@{Calendars.viewCalendar(_calendar.id, _day.date)}"
		#{if _day?.containsEvents}
			class="hasEvents"
		#{/if}>
		${_day.getNumber()}
	</a>
</td>