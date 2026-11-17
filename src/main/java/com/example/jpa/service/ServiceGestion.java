package com.example.jpa.service;

import com.example.jpa.dao.SalleDao;
import com.example.jpa.dao.UtilisateurDao;
import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Service métier : crée l'EntityManager, instancie les DAO et expose les opérations.
 * Appeler fermer() pour libérer les ressources.
 * TR: İş servisi: EntityManager'ı oluşturur, DAO'ları başlatır ve işlemleri sunar.
 * TR: Kaynakları serbest bırakmak için fermer() çağrılmalıdır.
 */
public class ServiceGestion {

    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final SalleDao salleDao;
    private final UtilisateurDao utilisateurDao;

    public ServiceGestion() {
        // TR: persistence.xml içindeki "gestionPU" birimini kullanarak fabrikayı oluştur.
        emf = Persistence.createEntityManagerFactory("gestionPU");
        em = emf.createEntityManager();
        // TR: Aynı EntityManager'ı paylaşan DAO'lar.
        salleDao = new SalleDao(em);
        utilisateurDao = new UtilisateurDao(em);
    }

    // TR: EntityManager ve fabrikayı kapatır (kaynak sızıntısını önler).
    public void fermer() {
        if (em.isOpen()) em.close();
        if (emf.isOpen()) emf.close();
    }

    // ----- Salles -----
    // ----- TR: Salon işlemleri -----

    // TR: Yeni salon ekler.
    public void ajouterSalle(Salle salle) {
        salleDao.create(salle);
    }

    // TR: Kimliğe göre salon bulur.
    public Salle trouverSalle(Long id) {
        return salleDao.find(id);
    }

    // TR: Salonu günceller.
    public Salle modifierSalle(Salle salle) {
        return salleDao.update(salle);
    }

    // TR: Salonu siler.
    public void supprimerSalle(Salle salle) {
        salleDao.delete(salle);
    }

    // TR: Tüm salonları listeler.
    public List<Salle> listerSalles() {
        return salleDao.findAll();
    }

    // TR: Minimum kapasiteye göre salonları listeler.
    public List<Salle> sallesAvecCapaciteMin(int capaciteMin) {
        return salleDao.findByCapaciteMin(capaciteMin);
    }

    // ----- Utilisateurs -----
    // ----- TR: Kullanıcı işlemleri -----

    // TR: Yeni kullanıcı ekler.
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurDao.create(utilisateur);
    }

    // TR: Kullanıcıyı siler.
    public void supprimerUtilisateur(Utilisateur utilisateur) {
        utilisateurDao.delete(utilisateur);
    }

    // TR: Tüm kullanıcıları listeler.
    public List<Utilisateur> listerUtilisateurs() {
        return utilisateurDao.findAll();
    }

    // TR: Belirli bir salona bağlı kullanıcıları listeler.
    public List<Utilisateur> utilisateursParSalle(String nomSalle) {
        return utilisateurDao.findBySalleNom(nomSalle);
    }

    // TR: Adı verilen metni içeren kullanıcıları listeler.
    public List<Utilisateur> utilisateursParNom(String fragment) {
        return utilisateurDao.findByNomContenant(fragment);
    }
}
