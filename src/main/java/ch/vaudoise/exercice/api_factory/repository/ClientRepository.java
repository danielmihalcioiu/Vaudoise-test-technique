/**
 * =============================================================
 *  File: ClientRepository.java
 *  Author: Daniel Mihalcioiu
 *  Description: Spring Data JPA repository for managing Client entities.
 *               Provides methods for retrieving and validating active clients.
 * =============================================================
 */

package ch.vaudoise.exercice.api_factory.repository;

import ch.vaudoise.exercice.api_factory.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Checks if an active client already exists with the given email address.
     *
     * @param email the email to check
     * @return true if an active client with this email exists, false otherwise
     */
    boolean existsByEmailAndActiveTrue(String email);

    /**
     * Retrieves all clients currently marked as active (not soft-deleted).
     *
     * @return list of active clients
     */
    List<Client> findByActiveTrue();
}
