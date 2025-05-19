package amazzal.youssef.examjee.repositories;

import amazzal.youssef.examjee.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}