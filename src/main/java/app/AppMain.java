package app;

import app.model.User;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

public class AppMain {
    public static void main(String[] args) {
        String token = "";
        String serverUrl = "http://localhost:8081"; // Proporciona la URL directamente aquí
        String username = generateRandomString(8);
        String password = generateRandomString(8);
        User user = new User(username, password);
        Gson gson = new Gson();
        String userJSON = gson.toJson(user);
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/login")) // Reemplaza esto con la URL de tu servidor
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(userJSON))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(loginRequest, HttpResponse.BodyHandlers.ofString());
            token = response.body();
            System.out.println("Respuesta del servidor Login: " + response.statusCode());
            System.out.println("Cuerpo de la respuesta: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }


        HttpRequest saludoRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/saludo?nombre=" + username))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(saludoRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Respuesta del servidor Saludo: " + response.statusCode());
            System.out.println("Cuerpo de la respuesta: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
