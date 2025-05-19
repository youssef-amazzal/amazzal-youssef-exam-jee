package amazzal.youssef.examjee.repositories;

import amazzal.youssef.examjee.entities.Credit;
import amazzal.youssef.examjee.entities.StatutCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    List<Credit> findByClientId(Long clientId);
    List<Credit> findByStatut(StatutCredit statut);
}