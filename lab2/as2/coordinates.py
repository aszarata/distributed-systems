import requests

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
