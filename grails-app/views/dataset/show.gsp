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
	<div class="hero-unit" id="chart" style="display: none">
	</div>
	<div class="row">
		<div class="span6">
			<h2>Top Runs</h2>
			<ol>
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
		var options = {
			chart: {
				renderTo: 'chart',
				type: 'spline',
				width: 1050,
				style: {
					margin: '0 auto'
				},
				zoomType: 'x'
			},
			title: {
				text: 'Top Runs'
			},
			credits: {
				enabled: false
			},
			xAxis: {
				reversed: false,
				title: {
					enabled: true,
					text: 'Time (hours)'
				},
				labels: {
					formatter: function() {
						return this.value;
					}
				},
				maxPadding: 0.05,
				showLastLabel: true
			},
			yAxis: {
				type: 'logarithmic',
				title: {
					text: 'Score'
				},
				labels: {
					formatter: function() {
						return this.value;
					}
				},
				lineWidth: 2
			},
			legend: {
				enabled: true
			},
			tooltip: {
				formatter: function() {
					return '' + this.x + ': ' + this.y + '';
				}
			},
			plotOptions: {
				spline: {
					marker: {
						enabled: false
					}
				}
			},
			series: []
		};
		chart = new Highcharts.Chart(options);

		<g:each in="${best}" var="run">
			$.getJSON('../api/runs/${run.id}/progress', function(data) {
				var series = {
					name: '${run.name}',
					data: []
				};
				$.each(data, function(i, e) {
					if (e.time >= 3600) {
						series.data.push([e.time / 60 / 60, e.score]);
					}
				});
				chart.addSeries(series);
				$('#chart').show();
			});
		</g:each>
	});
	</script>
</body>
</html>
