/*
 * A mock implementation of {@link WeatherService} for testing.
 * It returns a configured weather response or can simulate a failure.
 */
public class MockWeather implements WeatherService {
    private Weather response;
    private boolean shouldFail;

    // Creates a mock with a sample weather response.
    public MockWeather() {
        this(new Weather(18.5, 12.0, "2026-10-23T12:00"));
    }

    // Creates a mock with the specified weather response.
    public MockWeather(Weather response) {
        this.response = response;
    }

    // Sets the weather response this mock will return.
    public void setResponse(Weather response) {
        this.response = response;
    }

    // Enables or disables simulated failures.
    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }

    /*
     * Returns the configured response, unless failure simulation is enabled.
     * The coordinates are ignored by this mock.
     */
    @Override
    public Weather getCurrent(double latitude, double longitude) throws WeatherException {
        if (shouldFail) {
            throw new WeatherException("This is the mock service's intentional error.");
        }
        return response;
    }
}