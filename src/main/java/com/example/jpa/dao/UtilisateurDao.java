package com.example.jpa.dao;

import com.example.jpa.entities.Utilisateur;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * DAO spécifique à l'entité Utilisateur.
 * TR: Utilisateur varlığına özel DAO; genel DAO'yu genişletir.
 */
public class UtilisateurDao extends GenericDao<Utilisateur> {

    public UtilisateurDao(EntityManager em) {
        super(Utilisateur.class, em);
    }

    /**
     * Utilisateurs rattachés à la salle portant ce nom (jointure implicite u.salle).
     * TR: Verilen adı taşıyan salona bağlı kullanıcılar (u.salle üzerinden örtük birleştirme).
     */
    public List<Utilisateur> findBySalleNom(String nomSalle) {
        return em.createQuery("SELECT u FROM Utilisateur u WHERE u.salle.nom = :nomSalle", Utilisateur.class)
                 .setParameter("nomSalle", nomSalle)
                 .getResultList();
    }

    /**
     * Utilisateurs dont le nom contient la chaîne donnée.
     * TR: Adı verilen metni içeren kullanıcılar (LIKE '%metin%').
     */
    public List<Utilisateur> findByNomContenant(String fragment) {
        return em.createQuery("SELECT u FROM Utilisateur u WHERE u.nom LIKE :pattern", Utilisateur.class)
                 .setParameter("pattern", "%" + fragment + "%")
                 .getResultList();
    }
}
