package com.invoice.planner.entity;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.invoice.planner.enums.EtatDevis;
import com.invoice.planner.utils.AuditTable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entité représentant un devis
 */
@Entity
@Table(name = "devis")
public class Devis extends AuditTable implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private UUID trackingId;
    
    @Column(name = "reference", unique = true, nullable = false)
    private String reference;
    
    @Column(name = "nom_projet")
    private String nomProjet;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "createur_id")
    private User createur;
    
    @Column(name = "date_emission", nullable = false)
    private LocalDate dateEmission;
    
    @Column(name = "date_echeance")
    private LocalDate dateEcheance;
    
    @Column(name = "prix_total", nullable = false)
    private Double prixTotal = 0.0;
    
    @Column(name = "prix_ttc", nullable = true)
    private Double prixTTC = 0.0;
    
    @Column(name = "tva")
    private Double tva = 20.0; // Taux de TVA par défaut à 20%
    
    @Column(name = "remise")
    private Double remise = 0.0;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private EtatDevis statut;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<Prestation> prestations = new ArrayList<>();
    
    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DocumentLigne> lignes = new ArrayList<>();
    
    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Taxe> taxes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "facture_id")
    private Facture facture;
    
    // @PrePersist
    // public void prePersist() {
    //     if (this.trackingId == null) {
    //         this.trackingId = UUID.randomUUID();
    //     }
    //     if (this.dateEmission == null) {
    //         this.dateEmission = LocalDate.now();
    //     }
    // }
    
    // Constructeurs
    public Devis() {
    }
    
    public Devis(String reference, String nomProjet, Client client, User createur, 
                LocalDate dateEcheance, Double remise, EtatDevis statut, String description, LocalDate dateEmission) {
        this.reference = reference;
        this.nomProjet = nomProjet;
        this.client = client;
        this.createur = createur;
        this.dateEmission = dateEmission; //LocalDate.now();
        this.dateEcheance = dateEcheance;
        this.remise = remise;
        this.statut = statut;
        this.description = description;
        this.prixTotal = 0.0;
        this.prixTTC = 0.0;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
    
    public String getNomProjet() {
        return nomProjet;
    }
    
    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getCreateur() {
        return createur;
    }

    public void setCreateur(User createur) {
        this.createur = createur;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public Double getPrixTTC() {
        return prixTTC;
    }

    public void setPrixTTC(Double prixTTC) {
        this.prixTTC = prixTTC;
    }
    
    public Double getTva() {
        return tva;
    }
    
    public void setTva(Double tva) {
        this.tva = tva;
    }

    public Double getRemise() {
        return remise;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public EtatDevis getStatut() {
        return statut;
    }

    public void setStatut(EtatDevis statut) {
        this.statut = statut;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public List<Prestation> getPrestations() {
        return prestations;
    }
    
    public void setPrestations(List<Prestation> prestations) {
        this.prestations = prestations;
    }

    public List<DocumentLigne> getLignes() {
        return lignes;
    }

    public void setLignes(List<DocumentLigne> lignes) {
        this.lignes = lignes;
    }

    public List<Taxe> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<Taxe> taxes) {
        this.taxes = taxes;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    // Méthodes utilitaires pour la gestion des prestations
    public void addPrestation(Prestation prestation) {
        prestations.add(prestation);
        prestation.setDevis(this);
    }
    
    public void removePrestation(Prestation prestation) {
        prestations.remove(prestation);
        prestation.setDevis(null);
    }
    
    // Méthodes métier
    public double calculerPrixTotal() {
        double total = 0.0;
        
        // Calcul basé sur les prestations
        for (Prestation prestation : prestations) {
            total += prestation.calculerMontant();
        }
        
        // Calcul basé sur les lignes de document (pour rétrocompatibilité)
        if (total == 0.0 && !lignes.isEmpty()) {
            for (DocumentLigne ligne : lignes) {
                total += ligne.calculerMontant();
            }
        }
        
        if (remise != null && remise > 0) {
            total = total * (1 - remise / 100);
        }
        
        this.prixTotal = total;
        return total;
    }
    
    public double calculerPrixTTC() {
        double ht = calculerPrixTotal();
        double ttc = ht;
        
        // Si un taux de TVA global est défini
        if (tva != null && tva > 0) {
            ttc += ht * (tva / 100);
        } else {
            // Sinon, utiliser les taxes individuelles
            for (Taxe taxe : taxes) {
                ttc += ht * (taxe.getTaux() / 100);
            }
        }
        
        this.prixTTC = ttc;
        return ttc;
    }
    
    public Facture genererFacture() {
        // Implémentation de la création d'une facture à partir d'un devis
        Facture facture = new Facture();
        facture.setClient(this.client);
        facture.setReferenceDevis(this.reference);
        facture.setRemise(this.remise);
        
        // Copier les lignes du devis vers la facture
//        for (DocumentLigne ligne : this.lignes) {
//            DocumentLigne nouvelleLigne = new DocumentLigne();
//            nouvelleLigne.setDesignation(ligne.getDesignation());
//            nouvelleLigne.setQuantite(ligne.getQuantite());
//            nouvelleLigne.setPrixUnitaire(ligne.getPrixUnitaire());
//            nouvelleLigne.setFacture(facture);
//            facture.getLignes().add(nouvelleLigne);
//        }
        
        // Copier les taxes
//        for (Taxe taxe : this.taxes) {
//            Taxe nouvelleTaxe = new Taxe();
//            nouvelleTaxe.setNom(taxe.getNom());
//            nouvelleTaxe.setTaux(taxe.getTaux());
//            nouvelleTaxe.setFacture(facture);
//            facture.getTaxes().add(nouvelleTaxe);
//        }
        
        facture.calculerMontantHT();
        facture.calculerMontantTTC();
        
        return facture;
    }
    
    public File genererPDF() {
        // Implémentation de la génération du PDF
        return null;
    }
    
    public boolean envoyerParEmail() {
        // Implémentation de l'envoi par email
        return false;
    }
} 