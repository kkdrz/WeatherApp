
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="/static/css/bootstrap.min.css"> 
		<script type="text/javascript" src="/static/js/jquery-3.2.1.min.js"></script>
		<script type="text/javascript" src="/static/js/bootstrap.min.js"/></script>

	<title>${it.location}</title>
</head>
<body>
	<div class="container">

		<div class="col-md-offset-2 col-md-8 col-md-offset-2 panel panel-primary">
			<div class="col-md-12 text-center panel-heading">
				<h3>${it.location}</h3>
				<h4>${it.temperatureCelsius} °C</h4>
				<h4>${it.weather}</h5>
				<h5>Humidity: ${it.relativeHumidity}</h5>
				<h5>Wind: ${it.windString} (${it.windDirection})</h5>
			</div>
		</div>

	</div>
</body>
</html>
