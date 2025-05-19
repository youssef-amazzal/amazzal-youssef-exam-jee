package amazzal.youssef.examjee.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE_CREDIT", length = 4) // Adjust length as needed
public abstract class Credit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date dateDemande;
    @Enumerated(EnumType.STRING)
    private StatutCredit statut;
    @Temporal(TemporalType.DATE)
    private Date dateAcceptation;
    private double montant;
    private int dureeRemboursement; // en mois par exemple
    private double tauxInteret;

    @ManyToOne
    @JoinColumn(name = "client_id") // Explicitly naming the foreign key column
    private Client client;

    @OneToMany(mappedBy = "credit", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Remboursement> remboursements;
}