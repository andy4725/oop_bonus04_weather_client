import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenMeteoClient implements WeatherService {
    private final HttpClient httpClient;

    public OpenMeteoClient() {
        this(HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build());
    }

    OpenMeteoClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Weather getCurrent(double latitude, double longitude) throws WeatherException {
        String url = "https://api.open-meteo.com/v1/forecast"
                + "?latitude=" + latitude
                + "&longitude=" + longitude
                + "&current=temperature_2m,wind_speed_10m";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new WeatherException("Weather service returned HTTP " + response.statusCode());
            }

            return parseCurrentWeather(response.body());
        } catch (IOException e) {
            throw new WeatherException("Could not connect to the weather service.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WeatherException("The weather request was interrupted.", e);
        } catch(IllegalArgumentException e) {
            throw new WeatherException("The weather service returned invalid data.", e);
        }
    }

    // Extracts the fields we requested from the API's current object.
    static Weather parseCurrentWeather(String json) throws WeatherException {
        try {
            String current = findObject(json, "current");
            double temperature = findNumber(current, "temperature_2m");
            double windSpeed = findNumber(current, "wind_speed_10m");
            String time = findString(current, "time");
            return new Weather(temperature, windSpeed, time);
        } catch(IllegalArgumentException e) {
            throw new WeatherException("Could not read weather data from JSON.", e);
        }
    }

    // Finds an object-valued field and returns the text between its opening
    // and first closing brace.
    private static String findObject(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(key)
                + "\"\\s*:\\s*\\{([^}]*)}");
        Matcher matcher = pattern.matcher(json);

        // Report an error if the requested object field isn't present.
        if (!matcher.find()) {
            throw new IllegalArgumentException("Missing object: " + key);
        }
        return matcher.group(1);
    }

    // Finds a number-valued field, allowing an optional minus sign and decimal part.
    private static double findNumber(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(key)
                + "\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");
        Matcher matcher = pattern.matcher(json);

        // Report an error if the requested number field isn't present.
        if (!matcher.find()) {
            throw new IllegalArgumentException("Missing number: " + key);
        }

        // Convert the matched number text to a double.
        return Double.parseDouble(matcher.group(1));
    }

    // Finds a string-valued field and returns the text inside its quotes.
    private static String findString(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(key)
                + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json);

        // Report an error if the requested string field isn't present.
        if (!matcher.find()) {
            throw new IllegalArgumentException("Missing string: " + key);
        }
        return matcher.group(1);
    }
}
