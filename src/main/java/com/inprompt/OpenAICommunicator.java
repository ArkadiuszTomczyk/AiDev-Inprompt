package com.inprompt;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class OpenAICommunicator {
    String getAnswer(String question, String information, String suggestion) throws IOException {

        Properties prop = new Properties();
        try (InputStream input = Main.class.getResourceAsStream("/config.properties")) {
            prop.load(input);
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
        }
        String openAIKey = prop.getProperty("openai.api.key");
        String prompt = information + "\nQuestion: " + question + suggestion + "\nAnswer:";

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject json = new JSONObject();
        json.put("prompt", prompt);
        json.put("max_tokens", 60);

        RequestBody body = RequestBody.create(mediaType, json.toString());

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/engines/text-davinci-003/completions")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + openAIKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
