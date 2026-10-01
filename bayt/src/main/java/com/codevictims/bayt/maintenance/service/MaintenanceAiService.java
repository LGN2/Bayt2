package com.codevictims.bayt.maintenance.service;

import com.codevictims.bayt.maintenance.ai.MaintenanceAiClient;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceAiService {
  private final MaintenanceAiClient client;

  public MaintenanceAiService(MaintenanceAiClient client) {
    this.client = client;
  }

  public Suggestion generate(String description, String category, boolean urgent) {
    String result = client.suggest(description, category, urgent);
    return new Suggestion(result, "AI-generated; technician approval required");
  }

  public record Suggestion(String recommendation, String disclaimer) {}
}