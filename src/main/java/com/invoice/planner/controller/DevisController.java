package com.invoice.planner.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.invoice.planner.dto.request.DevisRequest;
import com.invoice.planner.dto.response.DevisResponse;
import com.invoice.planner.service.DevisService;
import com.invoice.planner.utils.ApiResponse;
import com.invoice.planner.entity.Devis;
import com.invoice.planner.repository.DevisRepository;
import com.invoice.planner.enums.EtatDevis;

@RestController
@RequestMapping("/api/deviss")
public class DevisController {
    
    private final DevisService service;
    private final DevisRepository devisRepository;

    
    public DevisController(DevisService service, DevisRepository devisRepository) {
        this.service = service;
        this.devisRepository = devisRepository;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<DevisResponse>> create(@RequestBody DevisRequest request) {
        DevisResponse response = service.create(request);
        return ResponseEntity.ok(new ApiResponse<>("Devis created successfully", response));
    }
    
    @PutMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<DevisResponse>> update(@PathVariable UUID trackingId, @RequestBody DevisRequest request) {
        DevisResponse response = service.update(trackingId, request);
        return ResponseEntity.ok(new ApiResponse<>("Devis updated successfully", response));
    }
    
    @DeleteMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID trackingId) {
        service.delete(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Devis deleted successfully", null));
    }
    
    @GetMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<DevisResponse>> findByTrackingId(@PathVariable UUID trackingId) {
        DevisResponse response = service.findByTrackingId(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Devis found successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<DevisResponse>>> findAll() {
        List<DevisResponse> responses = service.findAll();
        return ResponseEntity.ok(new ApiResponse<>("Deviss found successfully", responses));
    }

    @GetMapping("/createdBy")
    public ResponseEntity<ApiResponse<List<DevisResponse>>> findByCreatedBy() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<DevisResponse> responses = service.findByCreatedBy(email);
        return ResponseEntity.ok(new ApiResponse<>("Deviss found successfully", responses));
    }

// @GetMapping("/{trackingId}/status")
// public ResponseEntity<ApiResponse<String>> getStatus(@PathVariable UUID trackingId) {
//     DevisResponse response = service.findByTrackingId(trackingId);
//     String status = response.getStatut().toString();
    
//     return ResponseEntity.ok(new ApiResponse<>("Devis status retrieved successfully", status));
// }

@PutMapping("/{trackingId}/status")
public ResponseEntity<ApiResponse<String>> updateStatus(@PathVariable UUID trackingId, @RequestBody String status) {
    Devis devis = devisRepository.findByTrackingId(trackingId).orElseThrow(() -> new RuntimeException("Devis not found"));
    devis.setStatut(EtatDevis.valueOf(status));
    devisRepository.save(devis);
    return ResponseEntity.ok(new ApiResponse<>("Devis status updated successfully", status));
}
} 