<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>CONOP.io</title>
	</head>
	<body>
		<div class="row">
			<div class="span12">
				${events}
			</div>
		</div>
		<div class="row">
			<div class="span4">
				<h2>Datasets</h2>
				<ul>
					<g:set var="list" value="${datasets.collate(5)}"/>
					<g:each in="${list[0]}" var="dataset">
						<li><g:link controller="dataset" action="show" id="${dataset.id}">${dataset.name}</g:link></li>
					</g:each>
					<g:if test="${!list}">
						<li><em>No runs</em></li>
					</g:if>
				</ul>
				<g:if test="${list.size() > 1}">
					<g:link controller="dataset" action="list">See All</g:link>
				</g:if>
			</div>
			<div class="span4">
				<h2>Recent Runs</h2>
				<ol>
					<g:set var="recent" value="${runs.sort{it.created}.collate(5)[0]}"/>
					<g:each in="${recent}" var="run">
						<li><g:render template="/run-brief" model="[run: run]"/></li>
					</g:each>
					<g:if test="${!recent}">
						<li><em>No runs</em></li>
					</g:if>
				</ol>
			</div>
			<div class="span4">
				<h2>Running Now</h2>
				${active}
			</div>
		</div>
	</body>
</html>
