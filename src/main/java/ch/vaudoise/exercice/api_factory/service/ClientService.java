/**
 * =============================================================
 *  File: ClientService.java
 *  Author: Daniel Mihalcioiu
 *  Description: Service layer handling all business logic related
 *               to Client entities and their lifecycle.
 *               Includes creation, update, soft deletion, and validation.
 * =============================================================
 */

package ch.vaudoise.exercice.api_factory.service;

import ch.vaudoise.exercice.api_factory.entity.Client;
import ch.vaudoise.exercice.api_factory.entity.Contract;
import ch.vaudoise.exercice.api_factory.repository.ClientRepository;
import ch.vaudoise.exercice.api_factory.repository.ContractRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ContractRepository contractRepository;

    public ClientService(ClientRepository clientRepository, ContractRepository contractRepository) {
        this.clientRepository = clientRepository;
        this.contractRepository = contractRepository;
    }

    /**
     * Retrieves all active clients (not soft-deleted).
     *
     * @return list of active clients
     */
    public List<Client> getAllClients() {
        return clientRepository.findByActiveTrue();
    }

    /**
     * Finds a client by its ID.
     *
     * @param id the ID of the client
     * @return an Optional containing the client if found
     */
    public Optional<Client> getClient(Long id) {
        return clientRepository.findById(id);
    }

    /**
     * Saves a new client in the database after checking email uniqueness.
     *
     * @param client the client entity to save
     * @return the saved client entity
     * @throws IllegalArgumentException if the email is already in use
     */
    public Client saveClient(Client client) {
        if (clientRepository.existsByEmailAndActiveTrue(client.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + client.getEmail());
        }
        client.setActive(true);
        return clientRepository.save(client);
    }

    /**
     * Updates an existing client’s basic information (name, email, phone).
     *
     * @param id    the ID of the client to update
     * @param name  new name
     * @param email new email
     * @param phone new phone number
     * @return the updated client
     */
    public Client updateClient(Long id, String name, String email, String phone) {
        return clientRepository.findById(id)
            .map(client -> {
                client.setName(name);
                client.setEmail(email);
                client.setPhone(phone);
                return clientRepository.save(client);
            })
            .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    /**
     * Soft deletes a client.
     * - Marks the client as inactive (active = false)
     * - Updates all their active contracts’ endDate to the current date
     *
     * @param id the ID of the client to delete
     */
    @Transactional
    public void deleteClient(Long id) {
        clientRepository.findById(id).ifPresent(client -> {
            // Close all active contracts
            List<Contract> contracts = contractRepository.findAllContractsByClient(id);
            contracts.forEach(contract -> {
                if (contract.getEndDate() == null || contract.getEndDate().isAfter(LocalDate.now())) {
                    contract.setEndDate(LocalDate.now());
                }
            });
            contractRepository.saveAll(contracts);

            // Soft delete the client
            client.setActive(false);
            clientRepository.save(client);
        });
    }
}
