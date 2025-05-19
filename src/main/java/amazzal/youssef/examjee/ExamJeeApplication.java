package amazzal.youssef.examjee;

import amazzal.youssef.examjee.entities.*;
import amazzal.youssef.examjee.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    CommandLineRunner start(
            ClientRepository clientRepository,
            CreditRepository creditRepository,
            RemboursementRepository remboursementRepository,
            AppUserRepository appUserRepository,
            AppRoleRepository appRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            System.out.println("**************** Initializing Clients/Credits Data ****************");

            Client client1 = new Client();
            client1.setNom("John Doe");
            client1.setEmail("john.doe@example.com");
            clientRepository.save(client1);

            Client client2 = new Client();
            client2.setNom("Jane Smith");
            client2.setEmail("jane.smith@example.com");
            clientRepository.save(client2);


            CreditImmobilier creditImmobilier = new CreditImmobilier();
            creditImmobilier.setClient(client1);
            creditImmobilier.setDateDemande(new Date());
            creditImmobilier.setMontant(200000.0);
            creditImmobilier.setDureeRemboursement(240);
            creditImmobilier.setTauxInteret(0.035);
            creditImmobilier.setStatut(StatutCredit.EN_COURS);
            creditImmobilier.setTypeBienFinance(TypeBienFinance.MAISON);
            creditRepository.save(creditImmobilier);


            CreditPersonnel creditPersonnel = new CreditPersonnel();
            creditPersonnel.setClient(client1);
            creditPersonnel.setDateDemande(new Date());
            creditPersonnel.setMontant(15000.0);
            creditPersonnel.setDureeRemboursement(48);
            creditPersonnel.setTauxInteret(0.07);
            creditPersonnel.setStatut(StatutCredit.ACCEPTE);
            creditPersonnel.setDateAcceptation(new Date());
            creditPersonnel.setMotif("Achat voiture");
            creditRepository.save(creditPersonnel);


            CreditProfessionnel creditProfessionnel = new CreditProfessionnel();
            creditProfessionnel.setClient(client2);
            creditProfessionnel.setDateDemande(new Date());
            creditProfessionnel.setMontant(50000.0);
            creditProfessionnel.setDureeRemboursement(60);
            creditProfessionnel.setTauxInteret(0.05);
            creditProfessionnel.setStatut(StatutCredit.REJETE);
            creditProfessionnel.setMotif("Fonds de roulement");
            creditProfessionnel.setRaisonSocialeEntreprise("Smith & Co.");
            creditRepository.save(creditProfessionnel);


            Remboursement remboursement1 = new Remboursement();
            remboursement1.setCredit(creditPersonnel);
            remboursement1.setDateRemboursement(new Date());
            remboursement1.setMontant(350.0);
            remboursement1.setType(TypeRemboursement.MENSUALITE);
            remboursementRepository.save(remboursement1);

            Remboursement remboursement2 = new Remboursement();
            remboursement2.setCredit(creditPersonnel);
            remboursement2.setDateRemboursement(new Date());
            remboursement2.setMontant(350.0);
            remboursement2.setType(TypeRemboursement.MENSUALITE);
            remboursementRepository.save(remboursement2);

            System.out.println("---- Clients Initial Data ----");
            clientRepository.findAll().forEach(c -> {
                System.out.println("Client: " + c.getNom() + ", Email: " + c.getEmail());
            });

            System.out.println("---- Credits for client 1 (Initial Data) ----");
            creditRepository.findByClientId(client1.getId()).forEach(cr -> {
                System.out.println("Credit Type: " + cr.getClass().getSimpleName() + ", Montant: " + cr.getMontant());
            });
            System.out.println("**************** Clients/Credits Data Initialized ****************\n");


            System.out.println("**************** Initializing Security Data (Users/Roles) ****************");

            AppRole roleClient = appRoleRepository.findByRoleName("ROLE_CLIENT");
            if (roleClient == null) {
                roleClient = appRoleRepository.save(new AppRole(null, "ROLE_CLIENT"));
                System.out.println("Created role: ROLE_CLIENT");
            }

            AppRole roleEmploye = appRoleRepository.findByRoleName("ROLE_EMPLOYE");
            if (roleEmploye == null) {
                roleEmploye = appRoleRepository.save(new AppRole(null, "ROLE_EMPLOYE"));
                System.out.println("Created role: ROLE_EMPLOYE");
            }

            AppRole roleAdmin = appRoleRepository.findByRoleName("ROLE_ADMIN");
            if (roleAdmin == null) {
                roleAdmin = appRoleRepository.save(new AppRole(null, "ROLE_ADMIN"));
                System.out.println("Created role: ROLE_ADMIN");
            }


            if (appUserRepository.findByUsername("userClient") == null) {
                AppUser user1 = new AppUser();
                user1.setUsername("userClient");
                user1.setPassword(passwordEncoder.encode("12345"));
                user1.getAppRoles().add(roleClient);
                appUserRepository.save(user1);
                System.out.println("Created user: userClient with role ROLE_CLIENT");
            }

            if (appUserRepository.findByUsername("userEmploye") == null) {
                AppUser user2 = new AppUser();
                user2.setUsername("userEmploye");
                user2.setPassword(passwordEncoder.encode("12345"));
                user2.getAppRoles().add(roleEmploye);
                appUserRepository.save(user2);
                System.out.println("Created user: userEmploye with role ROLE_EMPLOYE");
            }

            if (appUserRepository.findByUsername("admin") == null) {
                AppUser adminUser = new AppUser();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.getAppRoles().addAll(List.of(roleAdmin, roleEmploye, roleClient));
                appUserRepository.save(adminUser);
                System.out.println("Created user: admin with roles ROLE_ADMIN, ROLE_EMPLOYE, ROLE_CLIENT");
            }
            System.out.println("**************** Security Data Initialized ****************");

            System.out.println("\n---- Verifying Security Data ----");
            appUserRepository.findAll().forEach(u -> {
                System.out.print("User: " + u.getUsername() + " | Roles: ");
                u.getAppRoles().forEach(r -> System.out.print(r.getRoleName() + " "));
                System.out.println();
            });

        };
    }
}