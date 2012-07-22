<div class="event">
	<i class="icon-star"></i> New best score of <span class="label">${event.score as int}</span> found by run
	<g:link controller="run" action="show" id="${event.run}">${event.runName}</g:link>
	for dataset <g:link controller="dataset" action="show" id="${event.dataset}">${event.dataset}</g:link>
</div>