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

    public List<Client> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .filter(Client::isActive)
                .toList();
    }


    public Optional<Client> getClient(Long id) {
        return clientRepository.findById(id);
    }

    public Client saveClient(Client client) {
        if (clientRepository.existsByEmailAndIsActive(client.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + client.getEmail());
        }
        client.setActive(true);
        return clientRepository.save(client);
    }



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

    @Transactional
    public void deleteClient(Long id) {
        clientRepository.findById(id).ifPresent(client -> {
            List<Contract> contracts = contractRepository.findByClientId(id);
            contracts.forEach(contract -> {
                if (contract.getEndDate() == null || contract.getEndDate().isAfter(LocalDate.now())) {
                    contract.setEndDate(LocalDate.now());
                }
            });
            contractRepository.saveAll(contracts);

            client.setActive(false);
            clientRepository.save(client);
        });
    }

}
