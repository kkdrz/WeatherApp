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
		#grad1 {
			background: -webkit-linear-gradient(bottom, rgba(16, 50, 104,0), rgba(16, 50, 104,1)); /* For Safari 5.1 to 6.0 */
			background: -o-linear-gradient(top,rgba(16, 50, 104,0), rgba(16, 50, 104,1)); /* For Opera 11.1 to 12.0 */
			background: -moz-linear-gradient(bottom, rgba(16, 50, 104,0), rgba(16, 50, 104,1)); /* For Firefox 3.6 to 15 */
			background: linear-gradient(to top, rgba(16, 50, 104,0), rgba(16, 50, 104,1)); /* Standard syntax (must be last) */
			padding-top: 8px;
		}

		#update {
			color: white;
			margin-bottom: 5px;
		}
	</style>
</head>

<body>

	<div id="grad1" style="min-height: 1000px">
		<div id="update">${it[0].weatherDate}</div>
		<div class="container">

			<div class="input-group">
				<span class="input-group-addon">City/Country:</span>
				<input id="city-search" class="form-control" placeholder="Search..." onkeyup="searchCity()"/>
			</div>

			<c:forEach var="weather" items="${it}" varStatus="count">
				<button type="button" class="col-xs-12 col-sm-12 col-md-12 btn btn-info"
						data-toggle="collapse" href="#w${count.index}">

					<div class="col-xs-12 col-sm-12 col-md-12">
						<h3>${weather.location}</h3>
					</div>	
					<div class="col-xs-12 col-sm-12 col-md-12">
						<h4 class="text-center">${weather.temperatureCelsius} °C</h4>
						<h5 class="text-center">${weather.weather}</h5>
					</div>

					<div id="w${count.index}" class="col-xs-12  col-sm-12 col-md-12 collapse">	
						<div><h4>Humidity: ${weather.relativeHumidity}</h4></div>
						<div><h4>Wind: ${weather.windString} (${weather.windDirection})</h4></div>
					</div>
				</button>
			</c:forEach>

		</div>
	</div>
</body>

<script>
	function searchCity() {

		input = document.getElementById("city-search");
		filter = input.value.toUpperCase();
		buttons = document.getElementsByTagName("button");

		for (i = 0; i < buttons.length; i++) {
			h3 = buttons[i].getElementsByTagName("h3")[0];
			if (h3) {
				if (h3.innerHTML.toUpperCase().indexOf(filter) > -1) {
					buttons[i].style.display = "";
				} else {
					buttons[i].style.display = "none";
				}
			}
		}
	}
</script>
</html>
