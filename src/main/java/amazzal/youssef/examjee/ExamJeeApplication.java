package amazzal.youssef.examjee;

import amazzal.youssef.examjee.entities.*;
import amazzal.youssef.examjee.repositories.ClientRepository;
import amazzal.youssef.examjee.repositories.CreditRepository;
import amazzal.youssef.examjee.repositories.RemboursementRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional; 

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class ExamJeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamJeeApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner start(ClientRepository clientRepository,
                            CreditRepository creditRepository,
                            RemboursementRepository remboursementRepository) {
        return args -> {
            // Create a client
            Client client1 = new Client();
            client1.setNom("John Doe");
            client1.setEmail("john.doe@example.com");
            clientRepository.save(client1);

            Client client2 = new Client();
            client2.setNom("Jane Smith");
            client2.setEmail("jane.smith@example.com");
            clientRepository.save(client2);

            // Create Credit Immobilier
            CreditImmobilier creditImmobilier = new CreditImmobilier();
            creditImmobilier.setClient(client1);
            creditImmobilier.setDateDemande(new Date());
            creditImmobilier.setMontant(200000);
            creditImmobilier.setDureeRemboursement(240); // 20 years
            creditImmobilier.setTauxInteret(0.035);
            creditImmobilier.setStatut(StatutCredit.EN_COURS);
            creditImmobilier.setTypeBienFinance(TypeBienFinance.MAISON);
            creditRepository.save(creditImmobilier);

            // Create Credit Personnel
            CreditPersonnel creditPersonnel = new CreditPersonnel();
            creditPersonnel.setClient(client1);
            creditPersonnel.setDateDemande(new Date());
            creditPersonnel.setMontant(15000);
            creditPersonnel.setDureeRemboursement(48); // 4 years
            creditPersonnel.setTauxInteret(0.07);
            creditPersonnel.setStatut(StatutCredit.ACCEPTE);
            creditPersonnel.setDateAcceptation(new Date());
            creditPersonnel.setMotif("Achat voiture");
            creditRepository.save(creditPersonnel);

            // Create Credit Professionnel
            CreditProfessionnel creditProfessionnel = new CreditProfessionnel();
            creditProfessionnel.setClient(client2);
            creditProfessionnel.setDateDemande(new Date());
            creditProfessionnel.setMontant(50000);
            creditProfessionnel.setDureeRemboursement(60); // 5 years
            creditProfessionnel.setTauxInteret(0.05);
            creditProfessionnel.setStatut(StatutCredit.REJETE);
            creditProfessionnel.setMotif("Fonds de roulement");
            creditProfessionnel.setRaisonSocialeEntreprise("Smith & Co.");
            creditRepository.save(creditProfessionnel);

            // Create Remboursements for Credit Personnel
            Remboursement remboursement1 = new Remboursement();
            remboursement1.setCredit(creditPersonnel);
            remboursement1.setDateRemboursement(new Date());
            remboursement1.setMontant(350);
            remboursement1.setType(TypeRemboursement.MENSUALITE);
            remboursementRepository.save(remboursement1);

            Remboursement remboursement2 = new Remboursement();
            remboursement2.setCredit(creditPersonnel);
            remboursement2.setDateRemboursement(new Date()); // Should be a future date ideally
            remboursement2.setMontant(350);
            remboursement2.setType(TypeRemboursement.MENSUALITE);
            remboursementRepository.save(remboursement2);

            // Fetch and print some data to verify
            System.out.println("---- Clients ----");
            clientRepository.findAll().forEach(System.out::println);

            System.out.println("---- Credits for client 1 ----");
            creditRepository.findByClientId(client1.getId()).forEach(System.out::println);

            System.out.println("---- Remboursements for credit personnel ----");
            remboursementRepository.findByCreditId(creditPersonnel.getId()).forEach(System.out::println);

            System.out.println("---- Credits ACCEPTES ----");
            creditRepository.findByStatut(StatutCredit.ACCEPTE).forEach(System.out::println);
        };
    }
}