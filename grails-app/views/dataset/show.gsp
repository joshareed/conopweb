<%@ page import="grails.converters.JSON" %>
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
		<i class="icon-share"></i> Download
	</g:link>
	<h1 style="margin-bottom: 10px">${dataset.name}</h1>
	<g:if test="${dataset.description}">
		<p style="margin-bottom: 30px">${dataset.description}</p>
	</g:if>
	<div class="hero-unit" id="chart"></div>
	<div class="row">
		<div class="span6">
			<h2>Top Runs</h2>
			<ol>
				<g:set var="best" value="${runs.sort{it.score}.collate(5)[0]}"/>
				<g:each in="${best}" var="run">
					<li><g:render template="/run-brief" model="[run: run]"/></li>
				</g:each>
				<g:if test="${!best}">
					<li><em>No runs</em></li>
				</g:if>
			</ol>
		</div>
		<div class="span6">
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
	</div>
	<script type="text/javascript" charset="utf-8">
	var chart;
	$(document).ready(function() {
		chart = new Highcharts.Chart({
			chart: {
				renderTo: 'chart',
				type: 'scatter',
				width: 1050,
				style: {
					margin: '0 auto'
				},
			},
			title: {
				text: 'Runs'
			},
			credits: {
				enabled: false
			},
			xAxis: {
				title: {
					enabled: true,
					text: 'Time (hours)'
				}
			},
			yAxis: {
				title: {
					enabled: true,
					text: 'Score'
				}
			},
			legend: {
				enabled: true
			},
<%
def grouped = runs.groupBy { it.simulation.objective }
series = []
grouped.each { k, v ->
	series << [
		name: k,
		data: v.collect { [
			g.formatNumber(format: '#.00', number: (it.elapsed / 3600)) as double, 
			g.formatNumber(format: '#.00', number: (it.score)) as double]
		}
	]
}
%>
			series: ${series as JSON}
		});
	});
	</script>
</body>
</html>
