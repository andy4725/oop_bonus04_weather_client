// Small presenter so display formatting can be tested without opening a Swing window.
public class WeatherPresenter {
    private final WeatherService weatherService;

    public WeatherPresenter(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

   public String loadSummary(double latitude, double longitude) throws WeatherException {
        Weather weather = weatherService.getCurrent(latitude, longitude);
        return String.format(
            "%.1f °C  |  Wind: %.1f km/h  |  %s",
            weather.temperature(),
            weather.windSpeed(),
            weather.time());
    }
}