<g:if test="${time > 3600}">
	${(time / 3600) as int} hours, ${((time - (Math.floor(time / 3600) * 3600)) / 60) as int} minutes
</g:if>
<g:elseif test="${time > 60}">
	${(time / 60) as int} minutes
</g:elseif>
<g:else>
	${time} seconds
</g:else>
