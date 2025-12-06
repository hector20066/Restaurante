package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author hectoracevedo
 */
public class ServiceOpenWeather {

    private static final String API_BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String apiKey = "c253ad3c68ce42fb8404724cd1ee9d0d";

    public String getClimate(String city) {
        try {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
            String urlString = String.format("%s?q=%s&appid=%s&units=metric&lang=es", API_BASE_URL, encodedCity, this.apiKey);

            URI uri = new URI(urlString);
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }

                    return response.toString();
                }
            } else {
                if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    return "{\"error\":\"Ciudad no encontrada\"}";
                } else {
                    return "{\"error\":\"Error al contactar la API. Codigo: " + responseCode + "\"";
                }
            }
        } catch (Exception e) {
            return "{\"error\":Exception: " + e.getMessage() + "\"";
        }
    }
}
