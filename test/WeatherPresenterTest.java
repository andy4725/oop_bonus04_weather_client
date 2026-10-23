import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WeatherPresenterTest {
    @Test
    void asksServiceForWeatherAndFormatsSummary() throws Exception {
        WeatherService service = mock(WeatherService.class);
        when(service.getCurrent(46.54, 24.56))
                .thenReturn(new Weather(18.5, 12.0, "2026-10-23T12:00"));

        WeatherPresenter presenter = new WeatherPresenter(service);

        assertEquals(
                "18.5 °C  |  Wind: 12.0 km/h  |  2026-10-23T12:00",
                presenter.loadSummary(46.54, 24.56));
        verify(service).getCurrent(46.54, 24.56);
    }
}