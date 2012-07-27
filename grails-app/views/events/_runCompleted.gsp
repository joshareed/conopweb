<div class="event">
	<i class="icon-ok"></i> Run <g:link controller="run" action="show" id="${event.run}">${event.runName ?: event.run}</g:link> completed 
	for dataset <g:link controller="dataset" action="show" id="${event.dataset}">${event.dataset}</g:link> with a score of 
	<span class="label">${event.score as int}</span>
	<span class="timestamp"><prettytime:display date="${event.ts}"/></span>
</div>