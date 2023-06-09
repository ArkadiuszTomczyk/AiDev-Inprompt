package com.inprompt;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.Getter;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Getter
class TaskObtainer {
    String myKey;
    String token;
    public TaskObtainer(String myKey) {
        this.myKey = myKey;
    }
    String getTask () throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest authRequest = null;
        try {
            authRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://zadania.aidevs.pl/token/inprompt"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(Json.createObjectBuilder()
                            .add("apikey", myKey)
                            .build().toString()))
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpResponse<String> authResponse = client.send(authRequest, HttpResponse.BodyHandlers.ofString());

        JsonObject jsonResponse = Json.createReader(new java.io.StringReader(authResponse.body())).readObject();
        token = jsonResponse.getString("token");

        HttpRequest taskRequest = HttpRequest.newBuilder()
                .uri(new URI("https://zadania.aidevs.pl/task/" + token))
                .GET()
                .build();

        HttpResponse<String> taskResponse = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        return taskResponse.body();
    }
}