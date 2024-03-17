from fastapi import FastAPI, Request, HTTPException, Form
from fastapi.responses import HTMLResponse
from datetime import datetime
import requests

from weather import get_weather_info

app = FastAPI()


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
        <form id="weatherForm" action="/weather/" method="post">
            <label for="city">Enter City:</label><br>
            <input type="text" id="city" name="city"><br><br>
            <label for="country">Enter Country (optional):</label><br>
            <input type="text" id="country" name="country"><br><br>
            <button type="submit">Submit</button>
        </form>
    </body>
    </html>
    """
    return HTMLResponse(content=html_content)


@app.post("/weather", response_class=HTMLResponse)
async def get_weather(request: Request, city: str=Form(), country: str=Form(None)):
    formatted_data = await get_weather_info(city, country)
    return HTMLResponse(content=formatted_data)

