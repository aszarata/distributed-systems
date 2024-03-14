from fastapi import FastAPI, Request
from fastapi.responses import HTMLResponse, RedirectResponse
from fastapi.templating import Jinja2Templates
import requests


app = FastAPI()
templates = Jinja2Templates(directory="templates")

def get_weather_meteo_source( city ):
    url = f"https://www.meteosource.com/api/v1/free/point?place_id={city}&sections=daily%2Chourly&language=en&units=metric&key=rlss8pvsw9hjaaxy40l5oo87ojy9bupc07v6eo0m"

    response = requests.get(url)
    
    if response.status_code == 200:
        return response.json()
    else:
        return {"error": "Failed to fetch data from the weather API."}
    
    
from fastapi.responses import HTMLResponse
from fastapi import Request, APIRouter

@app.get("/", response_class=HTMLResponse)
async def read_root(request: Request):
    html_content = """
    <!DOCTYPE html>
    <html>
    <head>
        <title>Enter City and Date</title>
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
            <button type="submit">Submit</button>
        </form>

        <script>
            document.getElementById("weatherForm").addEventListener("submit", function(event) {
                event.preventDefault();
                var city = document.getElementById("city").value;
                window.location.href = "/weather/" + encodeURIComponent(city);
            });
        </script>
    </body>
    </html>
    """
    return HTMLResponse(content=html_content)



@app.get("/weather/{city}", response_class=HTMLResponse)
async def show_weather(request: Request, city: str):
    weather_data = get_weather_meteo_source(city)

    formatted_data = format_info(weather_data, city)

    return HTMLResponse(content=formatted_data)


from datetime import datetime

from datetime import datetime

def format_info(weather_data, city):
    try:
        daily = weather_data['daily']

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
                <h1>Weather Information in {city.capitalize()}</h1>
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
            type_of_precipitation = day_data['all_day']['precipitation']['type']
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
        return f"<h2>Error: Incorrect city name '{city}'.</h2>"




