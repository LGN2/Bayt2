package com.codevictims.bayt.maintenance.controller;

import com.codevictims.bayt.maintenance.service.MaintenanceAiService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance/ai")
public class MaintenanceAiController {
  private final MaintenanceAiService service;

  public MaintenanceAiController(MaintenanceAiService service) {
    this.service = service;
  }

  @PostMapping("/suggestion")
  public ResponseEntity<MaintenanceAiService.Suggestion> suggestion(
      @Valid @RequestBody MaintenanceAiRequest request) {
    return ResponseEntity.ok(service.generate(request.description(), request.category(), request.urgent()));
  }

  public record MaintenanceAiRequest(
      @NotBlank @Size(max = 2000) String description,
      @NotBlank @Size(max = 100) String category,
      boolean urgent) {}
}