package ch.vaudoise.exercice.api_factory.repository;

import ch.vaudoise.exercice.api_factory.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByEmailAndIsActive(String email);
    List<Client> findByIsActive();
}
