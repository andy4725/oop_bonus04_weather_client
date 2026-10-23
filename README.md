# Weather Client

A small Swing weather app demonstrating Dependency Inversion. The UI receives
a `WeatherService`; it does not know about HTTP or Open-Meteo.

## Requirements

- Java 17+
- Maven

## Run with real Open-Meteo data

```bash
mvn compile exec:java
Run offline with mock data
mvn compile exec:java -Dexec.args="--mock"
Run tests
mvn test
Example API request
https://api.open-meteo.com/v1/forecast?latitude=46.54&longitude=24.56&current=temperature_2m,wind_speed_10m

Example response (values change over time):
{
  "latitude": 46.54,
  "longitude": 24.56,
  "current": {
    "time": "2026-10-23T12:00",
    "temperature_2m": 18.5,
    "wind_speed_10m": 12.0
  }
}