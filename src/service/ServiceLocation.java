package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author hectoracevedo
 */
public class ServiceLocation {

    private static final String API_URL = "http://ip-api.com/json/";

    public String getLocation() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            bufferedReader.close();
            connection.disconnect();

            return response.toString();
        } catch (Exception e) {
            return "{\"Error\": " + e.getMessage() + "\"}";
        }
    }

}
