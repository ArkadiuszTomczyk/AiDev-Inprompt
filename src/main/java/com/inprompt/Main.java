package com.inprompt;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import java.util.Properties;

public class Main {
	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		Properties prop = new Properties();
		try (InputStream input = Main.class.getResourceAsStream("/config.properties")) {
			prop.load(input);
		} catch (IOException e) {
			System.err.println("Error loading properties file: " + e.getMessage());
		}
		String myKey = prop.getProperty("api.key");

		TaskObtainer taskObtainer = new TaskObtainer(myKey);
		String task = taskObtainer.getTask();
		TaskProcessor taskProcessor = new TaskProcessor();
		String answer = taskProcessor.processTask(task);

		JSONObject jsonObj = new JSONObject(answer);
		JSONArray choicesArray = jsonObj.getJSONArray("choices");
		JSONObject firstChoiceObj = choicesArray.getJSONObject(0);
		String text = firstChoiceObj.getString("text");

		String cleanedAnswer = text.trim().replaceAll("[^a-zA-Z]", "");

		TaskReporter taskReporter = new TaskReporter();
		taskReporter.reportTask(taskObtainer.getToken(), cleanedAnswer);
	}
}