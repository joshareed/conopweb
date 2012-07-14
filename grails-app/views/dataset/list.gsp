<html>
<head>
	<meta name="layout" content="main"/>
	<title>CONOP.io :: Datasets</title>
</head>
<body>
	<h1>Datasets</h1>
	<p>
	<table class="table table-bordered table-striped">
		<thead>
			<tr>
				<th style="width: 25%">Name</th>
				<th>Description</th>
				<th style="width: 65px">Link</th>
			</tr>
		</thead>
		<tbody>
		<g:each in="${datasets}" var="dataset">
			<tr>
				<td><g:link controller="dataset" action="show" id="${dataset.id}">${dataset.name}</g:link></td>
				<td>${dataset.description}</td>
				<td>
					<g:if test="${dataset.url}">
						<a href="${dataset.url}">More Info</a>
					</g:if>
				</td>
			</tr>
		</g:each>
		</tbody>
	</table>
	</p>
</body>
</html>