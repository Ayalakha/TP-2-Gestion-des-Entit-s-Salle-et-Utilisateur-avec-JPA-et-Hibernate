package com.example.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Salle de réunion : identifiant, nom unique et capacité maximale.
 * TR: Toplantı salonu: benzersiz kimlik, benzersiz ad ve maksimum kapasite.
 */
// TR: @Entity bu sınıfı bir JPA varlığı (veritabanı tablosu) olarak işaretler.
@Entity
public class Salle {

    // TR: Birincil anahtar; değeri veritabanı tarafından otomatik üretilir (IDENTITY).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TR: Salon adı boş olamaz ve tabloda benzersiz olmalıdır.
    @Column(nullable = false, unique = true)
    private String nom;

    // TR: Salonun kapasitesi (kişi sayısı), boş olamaz.
    @Column(nullable = false)
    private int capacite;

    // Constructeurs
    // TR: Yapıcılar — argümansız yapıcı JPA için zorunludur.
    public Salle() {}

    // TR: Ad ve kapasite ile yeni bir salon oluşturur.
    public Salle(String nom, int capacite) {
        this.nom = nom;
        this.capacite = capacite;
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

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    // TR: Hata ayıklama ve ekrana yazdırma için okunabilir metin gösterimi.
    @Override
    public String toString() {
        return "Salle [id=" + id + ", nom=" + nom + ", capacite=" + capacite + "]";
    }
}
