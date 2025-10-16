/**
 * =============================================================
 *  File: ContractRepository.java
 *  Author: Daniel Mihalcioiu
 *  Description: Spring Data JPA repository for managing Contract entities.
 *               Includes queries for retrieving active and archived contracts.
 * =============================================================
 */

package ch.vaudoise.exercice.api_factory.repository;

import ch.vaudoise.exercice.api_factory.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    /**
     * Retrieves all active contracts for a specific client.
     * A contract is considered active if its end date is in the future or not set.
     *
     * @param clientId the ID of the client
     * @param date     the reference date (usually LocalDate.now())
     * @return list of active contracts for the client
     */
    @Query("""
        SELECT c FROM Contract c
        WHERE c.client.id = :clientId
        AND (c.endDate IS NULL OR c.endDate > :date)
    """)
    List<Contract> findActiveContractsByClient(@Param("clientId") Long clientId,
                                               @Param("date") LocalDate date);

    /**
     * Retrieves all contracts (active and ended) for a specific client.
     *
     * @param clientId the ID of the client
     * @return list of all contracts for the client
     */
    @Query("SELECT c FROM Contract c WHERE c.client.id = :clientId")
    List<Contract> findAllContractsByClient(@Param("clientId") Long clientId);
}
