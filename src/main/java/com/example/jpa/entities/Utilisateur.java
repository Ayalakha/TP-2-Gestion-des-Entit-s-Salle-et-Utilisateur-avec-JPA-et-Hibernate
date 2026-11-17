package com.example.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Utilisateur pouvant réserver ou accéder à une salle.
 * TR: Bir salonu rezerve edebilen veya salona erişebilen kullanıcı.
 */
// TR: @Entity bu sınıfı bir JPA varlığı olarak işaretler.
@Entity
public class Utilisateur {

    // TR: Otomatik üretilen birincil anahtar.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TR: Kullanıcı adı boş olamaz.
    @Column(nullable = false)
    private String nom;

    // TR: E-posta boş olamaz ve benzersiz olmalıdır.
    @Column(nullable = false, unique = true)
    private String email;

    // TR: Çoka-bir ilişki: birçok kullanıcı aynı salona bağlı olabilir.
    // TR: @JoinColumn, utilisateur tablosundaki yabancı anahtar sütununu (salle_id) belirtir.
    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    // Constructeurs
    // TR: Yapıcılar — argümansız yapıcı JPA için zorunludur.
    public Utilisateur() {}

    // TR: Ad, e-posta ve bağlı salon ile yeni bir kullanıcı oluşturur.
    public Utilisateur(String nom, String email, Salle salle) {
        this.nom = nom;
        this.email = email;
        this.salle = salle;
    }

    // Getters et Setters
    // TR: Getter ve Setter metotları.
    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    // TR: Okunabilir metin gösterimi; salon yoksa "aucune" (hiçbiri) yazar.
    @Override
    public String toString() {
        return "Utilisateur [id=" + id + ", nom=" + nom + ", email=" + email
                + ", salle=" + (salle != null ? salle.getNom() : "aucune") + "]";
    }
}
