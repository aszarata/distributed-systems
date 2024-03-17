from fastapi import FastAPI, Request, HTTPException
from fastapi.responses import HTMLResponse
from datetime import datetime
import requests

app = FastAPI()

async def find_coordinates(city, country=None):
    base_url = "https://nominatim.openstreetmap.org/search"
    params = {
        "q": f"{city}, {country}" if country else city,
        "format": "json",
    }
    try:
        response = requests.get(base_url, params=params)
        response.raise_for_status()
        data = response.json()
        if data:
            latitude = round(float(data[0]['lat']), 1)
            longitude = round(float(data[0]['lon']), 1)
            return latitude, longitude
        else:
            return {"error": f"Failed to find the city {city}."}
    except (requests.RequestException, ValueError) as e:
        return {"error": f"Failed to fetch data from OpenStreetMap: {e}"}

'rlss8pvsw9hjaaxy40l5oo87ojy9bupc07v6eo0m'


async def get_weather_weatherbit(lat, lon):
    url = f'https://api.weatherbit.io/v2.0/forecast/daily?lat={lat}&lon={lon}&key=d49e7889a75c4854aea06cafc55c8526"'
    try:
        response = requests.get(url)
        response.raise_for_status()
        return response.json()
    except (requests.RequestException, ValueError) as e:
        return {"error": f"Failed to fetch data from the weatherBit API: {e}"}


async def get_weather_meteo_source(lat, lon):
    url = f"https://www.meteosource.com/api/v1/free/point?lat={lat}&lon={lon}&sections=daily&units=metric&key=rlss8pvsw9hjaaxy40l5oo87ojy9bupc07v6eo0m"
    try:
        response = requests.get(url)
        response.raise_for_status()
        return response.json()
    except (requests.RequestException, ValueError) as e:
        return {"error": f"Failed to fetch data from the MeteoSource API: {e}"}


@app.get("/", response_class=HTMLResponse)
async def read_root(request: Request):
    html_content = """
    <!DOCTYPE html>
    <html>
    <head>
        <title>Enter City and Country</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f0f0f0;
                margin: 0;
                padding: 0;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
            }
            form {
                background-color: #fff;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                text-align: center;
            }
            label {
                font-weight: bold;
            }
            input[type="text"] {
                width: 200px;
                padding: 8px;
                margin: 8px 0;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-sizing: border-box;
            }
            button {
                padding: 10px 20px;
                background-color: #007bff;
                color: #fff;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }
            button:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>
        <form id="weatherForm" action="/weather/" method="get">
            <label for="city">Enter City:</label><br>
            <input type="text" id="city" name="city"><br><br>
            <label for="country">Enter Country (optional):</label><br>
            <input type="text" id="country" name="country"><br><br>
            <button type="submit">Submit</button>
        </form>

        <script>
            document.getElementById("weatherForm").addEventListener("submit", function(event) {
                event.preventDefault();
                var city = document.getElementById("city").value;
                var country = document.getElementById("country").value;
                window.location.href = "/weather/" + encodeURIComponent(city) + (country ? '?country=' + encodeURIComponent(country) : '');
            });
        </script>
    </body>
    </html>
    """
    return HTMLResponse(content=html_content)


@app.get("/weather/{city}", response_class=HTMLResponse)
async def get_weather(request: Request, city: str, country: str = None):
    coordinates = await find_coordinates(city, country)
    if "error" in coordinates:
        return HTMLResponse(content=f"<h2>Error: {coordinates['error']}</h2><form action='/' method='get'><button type='submit'>Return to Home Page</button></form>")
    
    weather_data1 = await get_weather_meteo_source(*coordinates)
    if "error" in weather_data1:
        return HTMLResponse(content=f"<h2>Error: {weather_data1['error']}</h2><form action='/' method='get'><button type='submit'>Return to Home Page</button></form>")
    


    weather_data2 = await get_weather_weatherbit(*coordinates)

    formatted_data = format_info(weather_data1, weather_data2, city, country)
    return HTMLResponse(content=formatted_data)
    



def format_info(weather_data_meteo, weather_data_bit, city, country):
    try:
        daily = weather_data_meteo['daily']

        temperatures = []
        max_temperatures = []
        min_temperatures = []
        for day_data in daily['data']:
            temperatures.append(day_data['all_day']['temperature'])
            max_temperatures.append(day_data['all_day']['temperature_max'])
            min_temperatures.append(day_data['all_day']['temperature_min'])
        
        average_temperature = round(sum(temperatures) / len(temperatures), 2)
        lowest_temperature = min(min_temperatures)
        highest_temperature = max(max_temperatures)

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
                <h1>Weather Information in {city.capitalize()}, {country.upper() if type(country)==str else ''}</h1>
                <p>Average temperature: {average_temperature} °C</p>
                <p>Highest temperature: {highest_temperature} °C</p>
                <p>Lowest temperature: {lowest_temperature} °C</p>
            </div>
            <table border="1">
                <tr>
                    <th>Day of Week</th>
                    <th>Day</th>
                    <th>Weather</th>
                    <th>Temperature</th>
                    <th>Min Temperature</th>
                    <th>Max Temperature</th>
                    <th>Type of Precipitation</th>
                    <th>Precipitation Total</th>
                </tr>
        """
        for day_data in daily['data']:
            day = day_data['day']
            weather = day_data['weather'].replace('_', ' ')
            temperature = day_data['all_day']['temperature']
            temperature_min = day_data['all_day']['temperature_min']
            temperature_max = day_data['all_day']['temperature_max']
            type_of_precipitation = '-' if day_data['all_day']['precipitation']['type'] == 'none' else day_data['all_day']['precipitation']['type']
            precipitation_total = day_data['all_day']['precipitation']['total']
            # Konwertowanie daty na dzień tygodnia
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
                    <td>{type_of_precipitation}</td>
                    <td>{precipitation_total} mm</td>
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

        day_data = weather_data_bit["data"]

        for day_data in daily['data']:
            day = day_data['day']
            weather = day_data['weather'].replace('_', ' ')
            temperature = day_data['all_day']['temperature']
            temperature_min = day_data['all_day']['temperature_min']
            temperature_max = day_data['all_day']['temperature_max']
            type_of_precipitation = '-' if day_data['all_day']['precipitation']['type'] == 'none' else day_data['all_day']['precipitation']['type']
            precipitation_total = day_data['all_day']['precipitation']['total']
            # Konwertowanie daty na dzień tygodnia
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
                    <td>{type_of_precipitation}</td>
                    <td>{precipitation_total} mm</td>
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
    
    except KeyError:
        return f"<h2>Failed to find the '{city}' on weather service.</h2><form action='/' method='get'><button type='submit'>Return to Home Page</button></form>"
