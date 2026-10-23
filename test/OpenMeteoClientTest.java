import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenMeteoClientTest {
    @Test
    void parsesValidJson() throws Exception {
        String json = """
                {
                  "current": {
                    "time": "2026-10-23T12:00",
                    "temperature_2m": 18.5,
                    "wind_speed_10m": 12.0
                  }
                }
                """;

        Weather weather = OpenMeteoClient.parseCurrentWeather(json);

        assertEquals(18.5, weather.temperature());
        assertEquals(12.0, weather.windSpeed());
        assertEquals("2026-10-23T12:00", weather.time());
    }

    @Test
    void reportsMissingCurrentObject() {
        assertThrows(WeatherException.class,
                () -> OpenMeteoClient.parseCurrentWeather("{\"hourly\": {}}"));
    }

    @Test
    void reportsMissingWeatherField() {
        String json = """
                {"current":{"time":"2026-10-23T12:00","temperature_2m":18.5}}
                """;

        assertThrows(WeatherException.class,
                () -> OpenMeteoClient.parseCurrentWeather(json));
    }
}