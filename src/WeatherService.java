public interface WeatherService {
    Weather getCurrent(double latitude, double longitude) throws WeatherException;
}
