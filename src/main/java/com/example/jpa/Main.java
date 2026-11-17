package com.example.jpa;

import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import com.example.jpa.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Démonstration de la gestion manuelle des transactions avec EntityManager.
 * TR: EntityManager ile işlemlerin elle yönetilmesinin gösterimi.
 */
public class Main {

    public static void main(String[] args) {
        // TR: EntityManager al ve işlem (transaction) nesnesini elde et.
        EntityManager em = JPAUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // TR: İşlemi başlat, salon ve kullanıcıları kaydet, sonra onayla.
            tx.begin();
            Salle salleA = new Salle("Salle A", 10);
            Salle salleB = new Salle("Salle B", 25);
            em.persist(salleA);
            em.persist(salleB);
            em.persist(new Utilisateur("Jean Dupont", "jean@example.com", salleA));
            em.persist(new Utilisateur("Marie Curie", "marie@example.com", salleB));
            tx.commit();

            // TR: Tüm salonları listele.
            System.out.println("=== Salles ===");
            em.createQuery("SELECT s FROM Salle s", Salle.class)
              .getResultList().forEach(System.out::println);

            // TR: Kapasitesi 20'den büyük salonları listele.
            System.out.println("=== Salles de plus de 20 places ===");
            em.createQuery("SELECT s FROM Salle s WHERE s.capacite > :minCapacite", Salle.class)
              .setParameter("minCapacite", 20)
              .getResultList().forEach(System.out::println);

            // TR: Adında "Jean" geçen kullanıcıları listele.
            System.out.println("=== Utilisateurs dont le nom contient 'Jean' ===");
            em.createQuery("SELECT u FROM Utilisateur u WHERE u.nom LIKE :pattern", Utilisateur.class)
              .setParameter("pattern", "%Jean%")
              .getResultList().forEach(System.out::println);
        } catch (Exception e) {
            // TR: Hata olursa ve işlem aktifse geri al.
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            // TR: Kaynakları her durumda kapat.
            em.close();
            JPAUtil.close();
        }
    }
}
