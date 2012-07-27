<div class="event">
	<i class="icon-plus"></i> New run <g:link controller="run" action="show" id="${event.run}">${event.runName ?: event.run}</g:link> started 
	for dataset <g:link controller="dataset" action="show" id="${event.dataset}">${event.dataset}</g:link>
	<span class="timestamp"><prettytime:display date="${event.ts}"/></span>
</div>