package com.example.jpa.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe utilitaire (bonne pratique sans EJB) :
 * une seule EntityManagerFactory pour l'application, des EntityManager à durée de vie courte.
 * TR: Yardımcı sınıf (EJB olmadan iyi uygulama):
 * TR: uygulama için tek bir EntityManagerFactory, kısa ömürlü EntityManager'lar.
 */
public final class JPAUtil {

    // TR: Uygulama boyunca paylaşılan tek fabrika örneği.
    private static EntityManagerFactory emf;

    // TR: Örneklenmesini engellemek için özel yapıcı.
    private JPAUtil() {}

    // TR: Fabrikayı ilk kullanımda oluşturur (lazy), sonra aynı örneği döndürür.
    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("gestionPU");
        }
        return emf;
    }

    // TR: Bir iş birimi için yeni EntityManager oluşturur; kullanımdan sonra kapatılmalıdır.
    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    // TR: Uygulama sonunda fabrikayı kapatır.
    public static synchronized void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
