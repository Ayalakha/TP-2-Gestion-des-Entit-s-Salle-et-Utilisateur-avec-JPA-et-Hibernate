package com.example.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * DAO générique : opérations CRUD avec gestion manuelle des transactions (RESOURCE_LOCAL).
 * TR: Genel DAO: işlemlerin (transaction) elle yönetildiği CRUD operasyonları (RESOURCE_LOCAL).
 */
public class GenericDao<T> {

    // TR: Yönetilen varlığın sınıfı (ör. Salle.class).
    private final Class<T> entityClass;
    // TR: Kalıcılık işlemlerini yapan EntityManager; alt sınıflar da erişebilir.
    protected EntityManager em;

    public GenericDao(Class<T> entityClass, EntityManager em) {
        this.entityClass = entityClass;
        this.em = em;
    }

    // TR: Yeni bir varlığı veritabanına kaydeder (INSERT).
    public void create(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();          // TR: İşlemi başlat
            em.persist(entity);  // TR: Varlığı kalıcı hale getir
            tx.commit();         // TR: İşlemi onayla
        } catch (RuntimeException e) {
            rollback(tx, "création", e); // TR: Hata durumunda geri al
            throw e;
        }
    }

    // TR: Birincil anahtara göre bir varlık arar; bulunamazsa null döner.
    public T find(Object id) {
        return em.find(entityClass, id);
    }

    // TR: Mevcut bir varlığı günceller (merge) ve yönetilen kopyayı döndürür.
    public T update(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T merged = em.merge(entity);
            tx.commit();
            return merged;
        } catch (RuntimeException e) {
            rollback(tx, "mise à jour", e);
            throw e;
        }
    }

    // TR: Bir varlığı siler; varlık yönetilmiyorsa önce merge ile bağlanır.
    public void delete(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            tx.commit();
        } catch (RuntimeException e) {
            rollback(tx, "suppression", e);
            throw e;
        }
    }

    // TR: Basit bir JPQL sorgusuyla bu türdeki tüm varlıkları getirir.
    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                 .getResultList();
    }

    /**
     * Annule la transaction si elle est encore active et trace l'erreur.
     * TR: İşlem hâlâ aktifse geri alır (rollback) ve hatayı konsola yazar.
     */
    private void rollback(EntityTransaction tx, String operation, RuntimeException e) {
        if (tx.isActive()) {
            tx.rollback();
        }
        System.err.println("Erreur lors de la " + operation + " de l'entité "
                + entityClass.getSimpleName() + " : " + e.getMessage());
    }
}
