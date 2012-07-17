<div class="run">
	<span class="label ${run.status == 'active' ? 'label-info' : ''} run-score">${run?.score ? run.score as int : '...'}</span>
	<g:link controller="run" action="show" id="${run.id}">${run.name}</g:link>
</div>