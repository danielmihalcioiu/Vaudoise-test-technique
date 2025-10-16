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

    @PostMapping("/person")
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person person) {
        return ResponseEntity.ok((Person) clientService.saveClient(person));
    }

    @PostMapping("/company")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.ok((Company) clientService.saveClient(company));
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        return clientService.getClient(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @Valid @RequestBody UpdateClientRequest body) {
        try {
            var updated = clientService.updateClient(id, body.getName(), body.getEmail(), body.getPhone());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Client> restoreClient(@PathVariable Long id) {
        Optional<Client> clientOpt = clientService.getClient(id);
        if (clientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Client client = clientOpt.get();

        if (client.isActive()) return ResponseEntity.ok(client);

        client.setActive(true);
        return ResponseEntity.ok(clientService.saveClient(client));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
