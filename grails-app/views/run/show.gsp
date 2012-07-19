<html>
<head>
	<meta name="layout" content="main"/>
	<title>Run :: ${run.name}</title>
	<script type="text/javascript" charset="utf-8">
	$(function() {
		$('a.toggle').click(function() {
			var target = $(this).attr('href');
			$(target).toggle();
			return false;
		});
	});
	</script>
</head>
<body>
	<h1 style="margin-bottom: 10px">${run.name}</h1>
	<div class="hero-unit" id="chart" style="display: none"></div>
	<div class="row">
		<div class="span6">
			<h3>Status: ${run.status}</h3>
			<ul>
			<g:if test="${run.status == 'active'}">
				<li>
					Current Score: <g:formatNumber format="#.00" number="${run.score}"/>
				</li>
				<li>
					Current Rank: ${rank} / ${runCount}
				</li>
				<li>
					Current Run Time: <g:render template="/time" model="[time: run.elapsed]"/>
				</li>
				<li>
					Last Progress: <g:render template="/time" model="[time: lastProgress]"/>
				</li>
				<g:if test="${run.simulation['stop/progress/time']}">
					<li>
						<g:set var="remaining" value="${((run.simulation['stop/progress/time'] as int) * 60) - lastProgress}"/>
						Estimated Time Remaining: <g:render template="/time" model="[time: remaining]"/>
					</li>
				</g:if>
			</g:if>
			<g:else>
				<li>
					Final Score: <g:formatNumber format="#.00" number="${run.score}"/>
				</li>
				<li>Final Rank:  ${rank} / ${runCount}</li>
				<li>
					Final Run Time: <g:render template="/time" model="[time: run.elapsed]"/>
				</li>
			</g:else>
			</ul>
		</div>
		<div class="span6">
			<h3>Simulation</h3>
			<ul>
				<li>Dataset: <g:link controller="dataset" action="show" id="${run.dataset}">${run.dataset}</g:link></li>
				<li>Mutator: ${run.simulation.mutator}</li>
				<li>Constraints: ${run.simulation.constraints}</li>
				<li>Objective: ${run.simulation.objective}</li>
				<li>Schedule: ${run.simulation.schedule} [${run.simulation['schedule/initial']}C 𝛥 ${run.simulation['schedule/delta']}, ${run.simulation['schedule/stepsPer']}/]
			</ul>
			<p>
				<a href="#raw-simulation" class="toggle">[Raw Simulation Spec]</a>
				<div class="well" id="raw-simulation" style="display: none">
					<g:each in="${run.simulation.sort()}" var="e">
						${e.key.replaceAll('/', '.')}: ${e.value}<br/>
					</g:each>
				</div>
			</p>
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
				text: '${run.name}'
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
				enabled: false
			},
			tooltip: {
				formatter: function() {
					return '' + this.x + ' hours: ' + this.y + '';
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

		$.getJSON('../api/runs/${run.id}/progress', function(data) {
			var series = {
				name: '${run.name}',
				data: []
			};
			$.each(data, function(i, e) {
				series.data.push([e.time / 60 / 60, e.score]);
			});
			chart.addSeries(series);
			$('#chart').show();
		});
	});
	</script>
</body>
</html>
