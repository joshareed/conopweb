<html>
<head>
	<meta name="layout" content="main"/>
	<title>Dataset :: ${dataset.name}</title>
	<style type="text/css" media="screen">
		li {
			margin-left: -20px;
			padding: 7px 0 7px 0;
			height: 20px;
			list-style-type: none;
		}
		li a {
			padding-top: 5px;
		}
		li .label {
			margin-right: 7px;
			margin-top: -5px;
			display: inline-block;
		}
	</style>
</head>
<body>
	<g:link url="${dataset.url}" class="pull-right" style="margin-top: 15px">
		<i class="icon-share"></i> More Info
	</g:link>
	<h1 style="margin-bottom: 10px">${dataset.name}</h1>
	<g:if test="${dataset.description}">
		<p style="margin-bottom: 30px">${dataset.description}</p>
	</g:if>
	<div class="hero-unit">
		<h1>Graph</h1>
	</div>
	<div class="row">
		<div class="span6">
			<h2>Recent Runs</h2>
			<ol>
				<g:each in="${recent}" var="run">
					<li><span class="label run-score">${run?.score ? run.score as int : '...'}</span><g:link controller="run" action="show" id="${run.id}">${run.name}</g:link></li>
				</g:each>
				<g:if test="${!recent}">
					<li><em>No runs</em></li>
				</g:if>
			</ol>
		</div>
		<div class="span6">
			<h2>Top Runs</h2>
			<ol>
				<g:each in="${best}" var="run">
					<li><span class="label run-score">${run?.score ? run.score as int : '...'}</span><g:link controller="run" action="show" id="${run.id}">${run.name}</g:link></li>
				</g:each>
				<g:if test="${!best}">
					<li><em>No runs</em></li>
				</g:if>
			</ol>
		</div>
	</div>
</body>
</html>
