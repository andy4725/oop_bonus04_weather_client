import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockWeatherTest {
    @Test
    void returnsConfiguredWeather() throws Exception {
        Weather expected = new Weather(21.0, 4.5, "2026-10-23T13:00");
        MockWeather mock = new MockWeather(expected);

        assertEquals(expected, mock.getCurrent(46.54, 24.56));
    }

    @Test
    void canIntentionallyFail() {
        MockWeather mock = new MockWeather();
        mock.setShouldFail(true);

        assertThrows(WeatherException.class,
                () -> mock.getCurrent(46.54, 24.56));
    }
}