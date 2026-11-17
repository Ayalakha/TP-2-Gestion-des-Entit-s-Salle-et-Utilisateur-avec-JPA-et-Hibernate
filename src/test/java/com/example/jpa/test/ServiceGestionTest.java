package com.example.jpa.test;

import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import com.example.jpa.service.ServiceGestion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * TR: ServiceGestion için JUnit 4 birim testleri (uygulama sunucusu ve EJB olmadan).
 */
public class ServiceGestionTest {

    private ServiceGestion service;

    // TR: Her testten önce yeni bir servis (ve boş veritabanı şeması) oluşturulur.
    @Before
    public void setUp() {
        service = new ServiceGestion();
    }

    // TR: Her testten sonra kaynaklar kapatılır (create-drop ile şema silinir).
    @After
    public void tearDown() {
        service.fermer();
    }

    // ----- Tests de l'énoncé -----
    // ----- TR: Ödev metnindeki testler -----

    // TR: Salon oluşturulup listede bulunabildiğini doğrular.
    @Test
    public void testCreationEtRechercheSalle() {
        Salle salle = new Salle("Salle Test", 20);
        service.ajouterSalle(salle);

        List<Salle> salles = service.listerSalles();
        assertFalse(salles.isEmpty());
        assertEquals("Salle Test", salles.get(0).getNom());
    }

    // TR: Bir salona bağlı kullanıcının kaydedildiğini ve ilişkinin korunduğunu doğrular.
    @Test
    public void testCreationUtilisateurAvecSalle() {
        Salle salle = new Salle("Salle B", 15);
        service.ajouterSalle(salle);

        Utilisateur utilisateur = new Utilisateur("Alice", "alice@example.com", salle);
        service.ajouterUtilisateur(utilisateur);

        List<Utilisateur> utilisateurs = service.listerUtilisateurs();
        assertFalse(utilisateurs.isEmpty());
        assertEquals("Alice", utilisateurs.get(0).getNom());
        assertEquals("Salle B", utilisateurs.get(0).getSalle().getNom());
    }

    // TR: Salon adına göre kullanıcı aramasının doğru sayıda sonuç döndürdüğünü doğrular.
    @Test
    public void testRechercheUtilisateursParSalle() {
        Salle salle = new Salle("Salle C", 30);
        service.ajouterSalle(salle);

        Utilisateur u1 = new Utilisateur("Bob", "bob@example.com", salle);
        Utilisateur u2 = new Utilisateur("Carol", "carol@example.com", salle);
        service.ajouterUtilisateur(u1);
        service.ajouterUtilisateur(u2);

        List<Utilisateur> utilisateurs = service.utilisateursParSalle("Salle C");
        assertEquals(2, utilisateurs.size());
    }

    // ----- Tests complémentaires -----
    // ----- TR: Ek testler -----

    // TR: Kimliğin otomatik üretildiğini ve find() ile bulunabildiğini doğrular.
    @Test
    public void testIdGenereEtFind() {
        Salle salle = new Salle("Salle D", 12);
        service.ajouterSalle(salle);

        assertNotNull(salle.getId());
        Salle trouvee = service.trouverSalle(salle.getId());
        assertNotNull(trouvee);
        assertEquals("Salle D", trouvee.getNom());
    }

    // TR: Salon kapasitesinin güncellenebildiğini doğrular.
    @Test
    public void testMiseAJourSalle() {
        Salle salle = new Salle("Salle E", 10);
        service.ajouterSalle(salle);

        salle.setCapacite(50);
        service.modifierSalle(salle);

        assertEquals(50, service.trouverSalle(salle.getId()).getCapacite());
    }

    // TR: Silinen salonun artık bulunamadığını doğrular.
    @Test
    public void testSuppressionSalle() {
        Salle salle = new Salle("Salle F", 8);
        service.ajouterSalle(salle);
        Long id = salle.getId();

        service.supprimerSalle(salle);

        assertNull(service.trouverSalle(id));
        assertTrue(service.listerSalles().isEmpty());
    }

    // TR: Minimum kapasite filtresinin doğru salonları döndürdüğünü doğrular.
    @Test
    public void testSallesParCapaciteMin() {
        service.ajouterSalle(new Salle("Petite", 5));
        service.ajouterSalle(new Salle("Moyenne", 20));
        service.ajouterSalle(new Salle("Grande", 100));

        List<Salle> salles = service.sallesAvecCapaciteMin(20);
        assertEquals(2, salles.size());
    }

    // TR: LIKE ile ada göre aramanın çalıştığını doğrular.
    @Test
    public void testRechercheUtilisateurParNom() {
        Salle salle = new Salle("Salle G", 10);
        service.ajouterSalle(salle);
        service.ajouterUtilisateur(new Utilisateur("Jean Dupont", "jean@example.com", salle));
        service.ajouterUtilisateur(new Utilisateur("Jeanne Martin", "jeanne@example.com", salle));
        service.ajouterUtilisateur(new Utilisateur("Paul", "paul@example.com", salle));

        assertEquals(2, service.utilisateursParNom("Jean").size());
    }

    // TR: Aynı adla ikinci salon eklenince benzersizlik kısıtı nedeniyle istisna
    // TR: fırlatıldığını ve işlemin geri alındığını doğrular.
    @Test
    public void testNomSalleUniqueProvoqueRollback() {
        service.ajouterSalle(new Salle("Doublon", 10));

        try {
            service.ajouterSalle(new Salle("Doublon", 20));
            fail("Une exception était attendue (contrainte unique sur nom)");
        } catch (PersistenceException e) {
            // attendu : la transaction a été annulée par le DAO
            // TR: beklenen durum: işlem DAO tarafından geri alındı
        }
    }
}
