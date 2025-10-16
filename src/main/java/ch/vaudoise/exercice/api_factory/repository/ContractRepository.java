package ch.vaudoise.exercice.api_factory.repository;

import ch.vaudoise.exercice.api_factory.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByClientIdAndEndDateAfterOrEndDateIsNull(Long clientId, LocalDate date, Long clientId2);
    List<Contract> findByClientId(Long clientId);
}
