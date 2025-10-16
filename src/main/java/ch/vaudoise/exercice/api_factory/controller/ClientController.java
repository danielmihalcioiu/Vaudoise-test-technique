/**
 * =============================================================
 *  File: ClientController.java
 *  Author: Daniel Mihalcioiu
 *  Description: REST Controller for managing clients (persons and companies).
 *               Provides CRUD operations and restoration of soft-deleted clients.
 * =============================================================
 */

package ch.vaudoise.exercice.api_factory.controller;

import ch.vaudoise.exercice.api_factory.dto.UpdateClientRequest;
import ch.vaudoise.exercice.api_factory.entity.Client;
import ch.vaudoise.exercice.api_factory.entity.Person;
import ch.vaudoise.exercice.api_factory.entity.Company;
import ch.vaudoise.exercice.api_factory.service.ClientService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Creates a new individual client.
     *
     * @param person the person data to create
     * @return the created person entity
     */
    @PostMapping("/person")
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person person) {
        return ResponseEntity.ok((Person) clientService.saveClient(person));
    }

    /**
     * Creates a new company client.
     *
     * @param company the company data to create
     * @return the created company entity
     */
    @PostMapping("/company")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.ok((Company) clientService.saveClient(company));
    }

    /**
     * Retrieves all active clients.
     *
     * @return list of active clients
     */
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    /**
     * Retrieves a specific client by ID.
     *
     * @param id the ID of the client
     * @return the client entity or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        return clientService.getClient(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates a client’s basic information (name, email, phone).
     * Fields such as birthDate (for Person) and companyIdentifier (for Company)
     * cannot be updated.
     *
     * @param id   the ID of the client to update
     * @param body the new client data
     * @return the updated client entity or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @Valid @RequestBody UpdateClientRequest body) {
        try {
            var updated = clientService.updateClient(id, body.getName(), body.getEmail(), body.getPhone());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Restores a soft-deleted client (sets active = true).
     *
     * @param id the ID of the client to restore
     * @return the restored client or 404 if not found
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Client> restoreClient(@PathVariable Long id) {
        Optional<Client> clientOpt = clientService.getClient(id);
        if (clientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Client client = clientOpt.get();

        // If already active, return as is
        if (client.isActive()) return ResponseEntity.ok(client);

        client.setActive(true);
        return ResponseEntity.ok(clientService.saveClient(client));
    }

    /**
     * Soft deletes a client:
     * - Marks the client as inactive.
     * - Closes all active contracts (sets their endDate to today).
     *
     * @param id the ID of the client to delete
     * @return HTTP 204 (no content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
