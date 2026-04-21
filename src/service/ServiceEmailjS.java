/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Andres Cardenas
 */
public class ServiceEmailjS {
    
    private static final String API_URL = "https://api.emailjs.com/api/v1.0/email/send";
    private static final String SERVICE_ID = "service_pf8gdkc";
    private static final String TEMPLATE_ID = "template_f9qhkbg";
    private static final String PUBLIC_KEY = "11brmFbS2m4tV-bsn";
    private static final String PRIVATE_KEY = "d04SJlK22jREZtLOCAaa8";

    public String sendEmail(String name, String email, List<String> details, String totalSale) {
        try {
            String json = createJson(name, email, details, totalSale);

            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.getBytes("UTF-8"));
            }

            int responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(responseCode == 200 ? connection.getInputStream() : connection.getErrorStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();

            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String createJson(String name, String email, List<String> details, String totalSale) {
        String detail = String.join("\\n", details);

        return "{"
                + "\"service_id\":\"" + SERVICE_ID + "\","
                + "\"template_id\":\"" + TEMPLATE_ID + "\","
                + "\"user_id\":\"" + PUBLIC_KEY + "\","
                + "\"accessToken\":\"" + PRIVATE_KEY + "\","
                + "\"template_params\":{"
                + "\"cliente_nombre\":\"" + name + "\","
                + "\"email\":\"" + email + "\","
                + "\"detalle_pedido\":\"" + detail + "\","
                + "\"total\":\"" + totalSale + "\","
                + "\"fecha\":\"" + LocalDate.now() + "\""
                + "}"
                + "}";
    }

}

