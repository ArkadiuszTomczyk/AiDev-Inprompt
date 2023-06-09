package com.inprompt;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TaskReporter {
    void reportTask(String token, String answer) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        JsonObject ans = Json.createObjectBuilder().add("answer", answer).build();

        HttpRequest answerRequest = HttpRequest.newBuilder()
                .uri(new URI("https://zadania.aidevs.pl/answer/" + token))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(ans.toString()))
                .build();

        HttpResponse<String> answerResponse = client.send(answerRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Answer response code: " + answerResponse.statusCode());
    }
}
