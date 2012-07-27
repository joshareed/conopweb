<div class="event">
	<i class="icon-file"></i> New dataset <g:link controller="run" action="show" id="${event.dataset}">${event.datasetName ?: event.dataset}</g:link> created
	<span class="timestamp"><prettytime:display date="${event.ts}"/></span>
</div>