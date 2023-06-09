package com.inprompt;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskProcessor {
    String processTask(String task) throws IOException {
        JSONObject taskJson = new JSONObject(task);
        String question = taskJson.getString("question");

        Pattern pattern = Pattern.compile("\\b[A-Z]\\w*\\b");
        Matcher matcher = pattern.matcher(question);
        String name = "";
        String information = "";
        String suggestion = " Answer in one word, with no additional explanations.";

        if (matcher.find()) {
            name = matcher.group();
        }

        JSONArray jsonArray = taskJson.getJSONArray("input");
        List<String> list = jsonArray.toList().stream()
                .map(Object::toString)
                .toList();

        String finalName = name;
        if (!name.isEmpty()) {
            information = list.stream()
                    .filter(s -> s.contains(finalName))
                    .findFirst()
                    .orElse(null);
        }
        OpenAICommunicator openAICommunicator = new OpenAICommunicator();
        return openAICommunicator.getAnswer(question, information, suggestion);
    }
}
