import javax.swing.SwingUtilities;
import java.util.Arrays;

public class App {
    public static void main(String[] args) throws Exception {
        WeatherService service = Arrays.asList(args).contains("--mock")
                    ? new MockWeather()
                    : new OpenMeteoClient();

        SwingUtilities.invokeLater(() -> {
            WeatherWindow window = new WeatherWindow(service);
            window.setVisible(true);
        });
    }
}
