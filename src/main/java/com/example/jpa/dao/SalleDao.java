package com.example.jpa.dao;

import com.example.jpa.entities.Salle;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * DAO spécifique à l'entité Salle.
 * TR: Salle varlığına özel DAO; genel DAO'yu genişletir.
 */
public class SalleDao extends GenericDao<Salle> {

    public SalleDao(EntityManager em) {
        super(Salle.class, em);
    }

    /**
     * Salles dont la capacité est supérieure ou égale à capaciteMin.
     * TR: Kapasitesi capaciteMin değerine eşit veya büyük olan salonlar.
     */
    public List<Salle> findByCapaciteMin(int capaciteMin) {
        // TR: Adlandırılmış parametre (:capaciteMin) okunabilirliği ve güvenliği artırır.
        return em.createQuery("SELECT s FROM Salle s WHERE s.capacite >= :capaciteMin", Salle.class)
                 .setParameter("capaciteMin", capaciteMin)
                 .getResultList();
    }

    /**
     * Salles dont la capacité est strictement supérieure à minCapacite.
     * TR: Kapasitesi minCapacite değerinden kesinlikle büyük olan salonlar.
     */
    public List<Salle> findByCapaciteSuperieureA(int minCapacite) {
        return em.createQuery("SELECT s FROM Salle s WHERE s.capacite > :minCapacite", Salle.class)
                 .setParameter("minCapacite", minCapacite)
                 .getResultList();
    }
}
