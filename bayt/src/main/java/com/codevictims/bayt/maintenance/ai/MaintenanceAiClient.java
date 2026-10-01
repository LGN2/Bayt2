package com.codevictims.bayt.maintenance.ai;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MaintenanceAiClient {
  private final RestClient client;
  private final String apiKey;
  private final String model;

  public MaintenanceAiClient(
      RestClient.Builder builder,
      @Value("${app.ai.key:}") String apiKey,
      @Value("${app.ai.model:gpt-4.1-mini}") String model) {
    this.client = builder.baseUrl("https://api.openai.com/v1").build();
    this.apiKey = apiKey;
    this.model = model;
  }

  public String suggest(String description, String category, boolean urgent) {
    if (apiKey == null || apiKey.isBlank()) {
      throw new IllegalStateException("AI_API_KEY is required for maintenance AI suggestions");
    }

    String prompt = "Maintenance issue: " + description
        + "\nCategory: " + category
        + "\nUrgent: " + urgent
        + "\nProvide a concise diagnosis, priority, safety warning, and recommended next action.";

    Map<String, Object> body = Map.of(
        "model", model,
        "temperature", 0.2,
        "messages", List.of(
            Map.of("role", "system", "content", "You are a property maintenance assistant. Never claim certainty. Recommend a qualified technician for safety risks."),
            Map.of("role", "user", "content", prompt)));

    Map<?, ?> response = client.post()
        .uri("/chat/completions")
        .header("Authorization", "Bearer " + apiKey)
        .contentType(MediaType.APPLICATION_JSON)
        .body(body)
        .retrieve()
        .body(Map.class);

    if (response == null || !(response.get("choices") instanceof List<?> choices) || choices.isEmpty()) {
      throw new IllegalStateException("AI provider returned an empty response");
    }
    Object first = choices.getFirst();
    if (!(first instanceof Map<?, ?> choice) || !(choice.get("message") instanceof Map<?, ?> message)) {
      throw new IllegalStateException("AI provider returned an invalid response");
    }
    return String.valueOf(message.get("content"));
  }
}