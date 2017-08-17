<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>WeatherService</title>
		
		<link rel="stylesheet" href="/static/css/bootstrap.min.css"> 
		<link rel="stylesheet" href="/static/css/easy-autocomplete.min.css"> 
		
		<script type="text/javascript" src="/static/js/jquery-3.2.1.min.js"></script>
		<script type="text/javascript" src="/static/js/bootstrap.min.js"/></script>
		<script type="text/javascript" src="/static/js/jquery.easy-autocomplete.min.js"></script> 
	
	<style>


	</style>
	
</head>

<body>

	<div class="container">

		<div class="btn-group" id="weather-container">

			<c:forEach var="weather" items="${it}" varStatus="count">
				<button type="button" class="col-md-6 btn btn-info"
						data-toggle="collapse" data-parent="#weather-container" href="#w${count.index}">
					<div class="row">
						<div class="col-md-12">
							<h3>${weather.location}</h3>
						</div>
					</div>
					<div class="row">	
						<div class="col-md-12">
							<h4 class="text-center">${weather.temperatureCelsius} °C</h4>
							<h5 class="text-center">${weather.weather}</h5>
						</div>
					</div>
					<div id="w${count.index}" class="collapse">	
						<div><h4>Humidity: ${weather.relativeHumidity}</h4></div>
						<div><h4>Wind: ${weather.windString} (${weather.windDirection})</h4></div>
					</div>
				</button>
			</c:forEach>

		</div>
	</div>

</body>
</html>
