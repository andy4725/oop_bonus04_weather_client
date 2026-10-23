import javax.swing.*;
import java.awt.*;

public class WeatherWindow extends JFrame {
    private final WeatherService weatherService;
    private final JComboBox<City> cityPicker;
    private final JButton refreshButton;
    private final JLabel temperatureLabel;
    private final JLabel windLabel;
    private final JLabel timeLabel;

    public WeatherWindow(WeatherService weatherService) {
        super("Weather Client");
        this.weatherService = weatherService;

        cityPicker = new JComboBox<>(new City[] {
            new City("Targe Mures", 46.54, 24.56),
            new City("Bucharest", 44.43, 26.10),
            new City("Cluj-Napoca", 46.77, 23.59),
            new City("Budapest", 47.50, 19.04),
            new City("Vienna", 48.21, 16.37)
        });

        refreshButton = new JButton("Refresh");
        temperatureLabel = new JLabel("Temperature: -");
        windLabel = new JLabel("Wind: -");
        timeLabel = new JLabel("Updated: -");

        buildWindow();
        refreshButton.addActionListener(event -> refreshWeather());
    }

    private void buildWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 240);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        panel.add(new JLabel("Choose a city:"));
        panel.add(cityPicker);
        panel.add(refreshButton);
        panel.add(temperatureLabel);
        panel.add(windLabel);
        panel.add(timeLabel);

        setContentPane(panel);
    }

    private void refreshWeather() {
        City city = (City) cityPicker.getSelectedItem();
        if(city == null) return;

        refreshButton.setEnabled(false);
        refreshButton.setText("Loading...");

        new SwingWorker<Weather, Void>() {
            @Override
            protected Weather doInBackground() throws Exception {
                return weatherService.getCurrent(city.latitude(), city.longitude());
            }

            @Override
            protected void done() {
                refreshButton.setEnabled(true);
                refreshButton.setText("Refersh");

                try {
                    Weather weather = get();
                    temperatureLabel.setText(
                        String.format("Temperature: %.1f °C", weather.temperature()));
                    windLabel.setText(
                            String.format("Wind: %.1f km/h", weather.windSpeed()));
                    timeLabel.setText("Updated: " + weather.time());
                } catch (Exception e) {
                    String message = e.getCause() == null ? e.getMessage() : e.getCause().getMessage();
                    JOptionPane.showMessageDialog(WeatherWindow.this, message, "Weather error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();;
    }

    private record City(String name, double latitude, double longitude) {
        @Override
        public String toString() {
            return name;
        }
    }
}
