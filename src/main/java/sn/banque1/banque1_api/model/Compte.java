package sn.banque1.banque1_api.model;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "comptes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// Rôle principal
// rendre la création d’objets plus lisible
// éviter les constructeurs très longs
// permettre de choisir seulement certains champs
@Builder

// Cette annotation génère automatiquement la méthode :
// qui sert à afficher l’objet sous forme de texte.
// Lombok génère toString()
// ➡️ MAIS il ne doit pas afficher le champ transactions.
@ToString(exclude = { "transactions" })
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long solde;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate dateCreation;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    private String adresse;

    @Column(unique = true, nullable = false, length = 20)
    private String telephone;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String numPiece;

    @Column(nullable = false)
    private String pin;

    @Builder.Default
    @Column(nullable = false)
    private boolean actif = true;

    @OneToMany(mappedBy = "compte")
    private List<Transaction> transactions;
}
