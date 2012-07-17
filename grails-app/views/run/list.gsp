<html>
<head>
	<meta name="layout" content="main"/>
	<title>CONOP.io :: Runs</title>
</head>
<body>
	<h1>Runs</h1>
	<p>
	<table class="table table-bordered table-striped">
		<thead>
			<tr>
				<th>Name</th>
				<th>Dataset</th>
				<th>Score</th>
				<th>Status</th>
			</tr>
		</thead>
		<tbody>
		<g:each in="${runs}" var="run">
			<tr>
				<td><g:link controller="run" action="show" id="${run.id}">${run.name}</g:link></td>
				<td><g:link controller="dataset" action="show" id="${run.dataset}">${run.dataset}</g:link></td>
				<td><g:formatNumber number="${run.score}" format="#.00"/></td>
				<td>
					<span class="label ${run.status == 'active' ? 'label-info' : ''}">${run.status}</span>
				</td>
			</tr>
		</g:each>
		</tbody>
	</table>
	</p>
</body>
</html>
