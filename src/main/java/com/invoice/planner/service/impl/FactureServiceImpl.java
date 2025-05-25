package com.invoice.planner.service.impl;

import com.invoice.planner.entity.Devis;
import com.invoice.planner.repository.DevisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.FactureRequest;
import com.invoice.planner.dto.response.FactureResponse;
import com.invoice.planner.exception.ResourceNotFoundException;
import com.invoice.planner.mapper.FactureMapper;
import com.invoice.planner.repository.FactureRepository;
import com.invoice.planner.service.FactureService;
import com.invoice.planner.entity.Facture;

@Service
@Transactional
public class FactureServiceImpl implements FactureService {
    
    private final FactureRepository repository;
    private final FactureMapper mapper;
    private final DevisRepository devisRepository;
    
    public FactureServiceImpl(FactureRepository repository, FactureMapper mapper, DevisRepository devisRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.devisRepository = devisRepository;
    }
    
    @Override
    @Transactional
    public FactureResponse create(FactureRequest request) {
        Facture entity = mapper.toEntity(request);
        entity.setTrackingId(UUID.randomUUID());
        Facture savedEntity = repository.save(entity);
        for(Devis devis : request.getDevis()){
            devis.setFacture(entity);
            devisRepository.save(devis);
        }

        return mapper.toResponse(savedEntity);
    }

    @Override
    @Transactional
    public FactureResponse createByDevisId(UUID trackingId) {

        Devis devis = devisRepository.findByTrackingId(trackingId).orElseThrow(() -> new ResourceNotFoundException("Devis", "trackingId", trackingId));
        Facture entity = new Facture();
        entity.addDevis(devis);
        FactureResponse response = mapper.toResponse(repository.save(entity));
        devis.setFacture(entity);

        return response;
    }

    @Override
    public FactureResponse update(UUID trackingId, FactureRequest request) {
        Facture entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Facture", "trackingId", trackingId));
        
        mapper.updateEntityFromRequest(request, entity);
        entity.setTrackingId(trackingId);
        Facture updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }
    
    @Override
    public void delete(UUID trackingId) {
        Facture entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Facture", "trackingId", trackingId));
        
        repository.delete(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public FactureResponse findByTrackingId(UUID trackingId) {
        Facture entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Facture", "trackingId", trackingId));
        
        return mapper.toResponse(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FactureResponse> findAll() {
        return repository.findAll().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FactureResponse> search(String term) {
        // Implémentation simple : retourne tous les résultats si le terme est vide
        if (term == null || term.isEmpty()) {
            return findAll();
        }
        
        // Recherche par le repository si disponible, sinon filtre basique
        try {
            return repository.search(term).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            // Fallback à une méthode de recherche basique
            return findAll().stream()
                .filter(response -> response.toString().toLowerCase().contains(term.toLowerCase()))
                .collect(Collectors.toList());
        }
    }
} 