<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>CONOP.io</title>
		<style type="text/css" media="screen">
			.row li {
				margin-left: -20px;
				padding: 5px 0 5px 0;
				height: 20px;
				list-style-type: none;
			}
			.row li a {
				padding-top: 5px;
			}
			.row li .label {
				margin-right: 7px;
				margin-top: -5px;
				display: inline-block;
			}
			.event {
				padding-bottom: 5px;
				border-bottom: 1px dashed #ddd;
				margin-bottom: 10px;
			}
			.event i {
				margin-right: 7px;
			}
			.event .label {
				margin-top: -5px;
				display: inline-block;
			}
			.timestamp {
				float: right;
				font-style: italic;
				color: #999;
			}
		</style>
	</head>
	<body>
		<div class="row" style="margin-bottom: 30px">
			<div class="span12">
				<h2 style="margin-bottom: 15px">Recent Activity</h2>
				<g:each in="${events}" var="event">
					<g:render template="/events/${event.type}" model="[event: event]"/>
				</g:each>
			</div>
		</div>
		<div class="row">
			<div class="span4">
				<h2>Datasets</h2>
				<ul>
					<g:set var="list" value="${datasets.collate(5)}"/>
					<g:each in="${list[0]}" var="dataset">
						<li><i class="icon-file"></i> <g:link controller="dataset" action="show" id="${dataset.id}">${dataset.name}</g:link></li>
					</g:each>
					<g:if test="${!list[0]}">
						<li><em>No datasets</em></li>
					</g:if>
				</ul>
				<g:if test="${list.size() > 1}">
					<g:link controller="dataset" action="list">See All</g:link>
				</g:if>
			</div>
			<div class="span4">
				<h2>Recent Runs</h2>
				<ol>
					<g:set var="list" value="${runs.collate(5)}"/>
					<g:each in="${list[0]}" var="run">
						<li><g:render template="/run-brief" model="[run: run]"/></li>
					</g:each>
					<g:if test="${!list[0]}">
						<li><em>No recent runs</em></li>
					</g:if>
				</ol>
				<g:if test="${list.size() > 1}">
					<g:link controller="run" action="list">See All</g:link>
				</g:if>
			</div>
			<div class="span4">
				<h2>Running Now</h2>
				<ol>
					<g:set var="list" value="${active.collate(5)}"/>
					<g:each in="${list[0]}" var="run">
						<li><g:render template="/run-brief" model="[run: run]"/></li>
					</g:each>
					<g:if test="${!list[0]}">
						<li><em>No active runs</em></li>
					</g:if>
				</ol>
				<g:if test="${list.size() > 1}">
					<g:link controller="run" action="list">See All</g:link>
				</g:if>
			</div>
		</div>
	</body>
</html>
