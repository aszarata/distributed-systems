import requests
from coordinates import find_coordinates
from formatting import format_info

async def get_weather_info(city, country=None):
    coordinates = await find_coordinates(city, country)
    if "error" in coordinates:
        return f"<h2>Error: {coordinates['error']}</h2><form action='/' method='get'><button type='submit'>Return to Home Page</button></form>"
    
    weather_data1 = await get_weather_meteo_source(*coordinates)
    if "error" in weather_data1:
        return f"<h2>Error: {weather_data1['error']}</h2><form action='/' method='get'><button type='submit'>Return to Home Page</button></form>"
    
    weather_data2 = await get_weather_weatherbit(*coordinates)
    if "error" in weather_data2:
        return f"<h2>Error: {weather_data1['error']}</h2><form action='/' method='get'><button type='submit'>Return to Home Page</button></form>"

    formatted_data = format_info(weather_data1, weather_data2, city)
    return formatted_data

async def get_weather_weatherbit(lat, lon):
    url = f'https://api.weatherbit.io/v2.0/forecast/daily?lat={lat}&lon={lon}&key=d49e7889a75c4854aea06cafc55c8526'
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
