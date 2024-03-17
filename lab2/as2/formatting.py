from datetime import datetime

def format_info(weather_data_meteo, weather_data_bit, city):
    daily_meteo = weather_data_meteo['daily']
    daily_bit = weather_data_bit['data'][:7]


    country = weather_data_bit['country_code']

    temperatures_meteo = []
    max_temperatures_meteo = []
    min_temperatures_meteo = []
    for day_data in daily_meteo['data']:
        temperatures_meteo.append(day_data['all_day']['temperature'])
        max_temperatures_meteo.append(day_data['all_day']['temperature_max'])
        min_temperatures_meteo.append(day_data['all_day']['temperature_min'])
    
    temperatures_bit = []
    max_temperatures_bit = []
    min_temperatures_bit = []
    for day_data in daily_bit:
        temperatures_bit.append(day_data['temp'])
        max_temperatures_bit.append(day_data['max_temp'])
        min_temperatures_bit.append(day_data['min_temp'])

    average_temperature_meteo = round(sum(temperatures_meteo) / len(temperatures_meteo), 2)
    lowest_temperature_meteo = min(min_temperatures_meteo)
    highest_temperature_meteo = max(max_temperatures_meteo)

    average_temperature_bit = round(sum(temperatures_bit) / len(temperatures_bit), 2)
    lowest_temperature_bit = min(min_temperatures_bit)
    highest_temperature_bit = max(max_temperatures_bit)

    average_temperature = round((average_temperature_bit + average_temperature_meteo)/2, 2)
    min_temperature = min(lowest_temperature_bit, lowest_temperature_meteo)
    max_temperature = max(highest_temperature_bit, highest_temperature_meteo)

    html_content = f"""
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Weather Information</title>
        <style>
            body {{
                font-family: Arial, sans-serif;
                background-color: #f0f0f0;
                margin: 0;
                padding: 0;
            }}
            .weather-info {{
                background-color: #fff;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                text-align: center;
            }}
            table {{
                margin-top: 20px;
                border-collapse: collapse;
                width: 100%;
            }}
            th, td {{
                padding: 8px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }}
            th {{
                background-color: #f2f2f2;
            }}
            button {{
                margin-top: 20px;
                padding: 10px 20px;
                background-color: #007bff;
                color: #fff;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }}
            button:hover {{
                background-color: #0056b3;
            }}
        </style>
    </head>
    <body>
    """
    html_content += f"""
        <div class="weather-info">
            <h1>Weather Information in {city.capitalize()}, {country}</h1>
            <p></p>  
            <p>Average temperature: {average_temperature} °C</p>
            <p>Highest temperature: {max_temperature} °C</p>
            <p>Lowest temperature: {min_temperature} °C</p>

            <h2>Meteosource</h2>
            <p>Average temperature: {average_temperature_meteo} °C</p>
            <p>Highest temperature: {highest_temperature_meteo} °C</p>
            <p>Lowest temperature: {lowest_temperature_meteo} °C</p>
        </div>
        <table border="1">
            <tr>
                <th>Day of Week</th>
                <th>Day</th>
                <th>Weather</th>
                <th>Temperature</th>
                <th>Min Temperature</th>
                <th>Max Temperature</th>
                <th>Precipitation Total</th>
            </tr>
    """
    for day_data in daily_meteo['data']:
        day = day_data['day']
        weather = day_data['weather'].replace('_', ' ')
        temperature = day_data['all_day']['temperature']
        temperature_min = day_data['all_day']['temperature_min']
        temperature_max = day_data['all_day']['temperature_max']
        precipitation_total = day_data['all_day']['precipitation']['total']
        date_str = f"{day}"
        day_of_week = datetime.strptime(date_str, "%Y-%m-%d").strftime("%A")
        
        html_content += f"""
            <tr>
                <td>{day_of_week}</td>
                <td>{day}</td>
                <td>{weather}</td>
                <td>{temperature} °C</td>
                <td>{temperature_min} °C</td>
                <td>{temperature_max} °C</td>
                <td>{precipitation_total} mm</td>
            </tr>
        """

    html_content += f"""
        </table>
        <div class="weather-info">
            <h2>Weatherbit</h2>
            <p>Average temperature: {average_temperature_bit} °C</p>
            <p>Highest temperature: {highest_temperature_bit} °C</p>
            <p>Lowest temperature: {lowest_temperature_bit} °C</p>
        </div>
        <table border="1">
            <tr>
                <th>Day of Week</th>
                <th>Day</th>
                <th>Weather</th>
                <th>Temperature</th>
                <th>Min Temperature</th>
                <th>Max Temperature</th>
                <th>Precipitation Total</th>
            </tr>
    """

    for day_data in daily_bit:
        day = day_data['datetime']
        weather = day_data['weather']['description']
        temperature = day_data['temp']
        temperature_min = day_data['min_temp']
        temperature_max = day_data['max_temp']
        precipitation_total = day_data['precip']

        date_str = f"{day}"
        day_of_week = datetime.strptime(date_str, "%Y-%m-%d").strftime("%A")
        
        html_content += f"""
            <tr>
                <td>{day_of_week}</td>
                <td>{day}</td>
                <td>{weather}</td>
                <td>{temperature} °C</td>
                <td>{temperature_min} °C</td>
                <td>{temperature_max} °C</td>
                <td>{round(precipitation_total, 2)} mm</td>
            </tr>
        """

    html_content += """
        </table>
        <form action="/" method="get">
            <button type="submit">Return to Home Page</button>
        </form>
    </body>
    </html>
    """

    return html_content