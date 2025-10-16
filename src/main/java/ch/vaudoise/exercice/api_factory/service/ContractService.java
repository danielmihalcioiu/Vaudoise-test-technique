/**
 * =============================================================
 *  File: ContractService.java
 *  Author: Daniel Mihalcioiu
 *  Description: Service layer responsible for managing contracts.
 *               Handles contract creation, updates, filtering, and cost aggregation.
 * =============================================================
 */

package ch.vaudoise.exercice.api_factory.service;

import ch.vaudoise.exercice.api_factory.dto.ContractView;
import ch.vaudoise.exercice.api_factory.entity.Client;
import ch.vaudoise.exercice.api_factory.entity.Contract;
import ch.vaudoise.exercice.api_factory.repository.ClientRepository;
import ch.vaudoise.exercice.api_factory.repository.ContractRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final ClientRepository clientRepository;

    public ContractService(ContractRepository contractRepository, ClientRepository clientRepository) {
        this.contractRepository = contractRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Saves a new contract, setting default start date if not provided.
     *
     * @param contract the contract entity to save
     * @return the saved contract
     */
    public Contract saveContract(Contract contract) {
        if (contract.getStartDate() == null) {
            contract.setStartDate(LocalDate.now());
        }
        return contractRepository.save(contract);
    }

    /**
     * Retrieves all active contracts for a given client.
     * A contract is considered active if endDate is in the future or null.
     *
     * @param clientId the client's ID
     * @return list of active contracts
     */
    public List<Contract> getActiveContractsForClient(Long clientId) {
        return contractRepository.findActiveContractsByClient(clientId, LocalDate.now());
    }

    /**
     * Retrieves all contracts (active and inactive) for a given client.
     *
     * @param clientId the client's ID
     * @return list of all contracts
     */
    public List<Contract> getAllContractsForClient(Long clientId) {
        return contractRepository.findAllContractsByClient(clientId);
    }

    /**
     * Updates the cost amount of a contract and refreshes its updateDate.
     *
     * @param contractId the ID of the contract to update
     * @param newAmount  the new cost amount
     * @return the updated contract
     */
    public Contract updateContractAmount(Long contractId, Double newAmount) {
        return contractRepository.findById(contractId).map(contract -> {
            contract.setCostAmount(newAmount);
            contract.setUpdateDate(java.time.LocalDateTime.now());
            return contractRepository.save(contract);
        }).orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    /**
     * Calculates the total cost amount of all active contracts for a client.
     *
     * @param clientId the client's ID
     * @return total active contract cost
     */
    public Double getActiveContractsTotal(Long clientId) {
        return getActiveContractsForClient(clientId).stream()
                .mapToDouble(Contract::getCostAmount)
                .sum();
    }

    /**
     * Finds a client by ID or returns null if not found.
     *
     * @param id the client's ID
     * @return the client entity or null
     */
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all contracts in the database (admin-level query).
     *
     * @return list of all contracts
     */
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    /**
     * Converts a Contract entity into a ContractView DTO for safe API exposure.
     *
     * @param c the contract entity
     * @return the corresponding ContractView DTO
     */
    public ContractView toView(Contract c) {
        return new ContractView(
                c.getId(),
                c.getCostAmount(),
                c.getStartDate(),
                c.getEndDate(),
                c.getClient().getId(),
                c.getClient().getName()
        );
    }
}
