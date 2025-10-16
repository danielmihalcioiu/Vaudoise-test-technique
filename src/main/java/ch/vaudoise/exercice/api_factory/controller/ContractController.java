/**
 * =============================================================
 *  File: ContractController.java
 *  Author: Daniel Mihalcioiu
 *  Description: REST Controller for managing insurance contracts.
 *               Handles creation, retrieval, filtering, and updates.
 * =============================================================
 */

package ch.vaudoise.exercice.api_factory.controller;

import ch.vaudoise.exercice.api_factory.dto.ContractDTO;
import ch.vaudoise.exercice.api_factory.dto.ContractView;
import ch.vaudoise.exercice.api_factory.entity.Contract;
import ch.vaudoise.exercice.api_factory.service.ContractService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    /**
     * Creates a new contract for an existing client.
     * Automatically sets startDate to current date if not provided.
     *
     * @param dto the contract creation data
     * @return the created contract view
     */
    @PostMapping
    public ResponseEntity<ContractView> createContract(@RequestBody ContractDTO dto) {
        if (dto.getClientId() == null) {
            return ResponseEntity.badRequest().build();
        }

        var client = contractService.getClientById(dto.getClientId());
        if (client == null) {
            return ResponseEntity.notFound().build();
        }

        Contract contract = new Contract();
        contract.setClient(client);
        contract.setCostAmount(dto.getCostAmount());
        contract.setStartDate(dto.getStartDate());
        contract.setEndDate(dto.getEndDate());

        Contract saved = contractService.saveContract(contract);
        return ResponseEntity.ok(ContractView.from(saved));
    }

    /**
     * Retrieves all contracts (active and inactive).
     *
     * @return list of all contracts
     */
    @GetMapping
    public ResponseEntity<List<ContractView>> getAllContracts() {
        var contracts = contractService.getAllContracts()
                .stream()
                .map(ContractView::from)
                .toList();
        return ResponseEntity.ok(contracts);
    }

    /**
     * Retrieves all active contracts for a client.
     * Optionally filters by updateDate if the "updatedAfter" parameter is provided.
     *
     * @param clientId     the client's ID
     * @param updatedAfter optional filter for last modification date
     * @return list of active (and optionally filtered) contract views
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ContractView>> getActiveContractsByClient(
            @PathVariable Long clientId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedAfter) {

        var contracts = contractService.getActiveContractsForClient(clientId);

        if (updatedAfter != null) {
            contracts = contracts.stream()
                    .filter(c -> c.getUpdateDate() != null && c.getUpdateDate().toLocalDate().isAfter(updatedAfter))
                    .toList();
        }

        var views = contracts.stream()
                .map(ContractView::from)
                .toList();

        return ResponseEntity.ok(views);
    }

    /**
     * Retrieves all contracts (active and ended) for a specific client.
     *
     * @param clientId the client's ID
     * @return list of all contract views
     */
    @GetMapping("/client/{clientId}/all")
    public ResponseEntity<List<ContractView>> getAllContractsByClient(@PathVariable Long clientId) {
        var contracts = contractService.getAllContractsForClient(clientId)
                .stream()
                .map(ContractView::from)
                .toList();
        return ResponseEntity.ok(contracts);
    }

    /**
     * Retrieves contracts updated after a given date for a specific client.
     * Only includes active contracts.
     *
     * @param clientId the client's ID
     * @param date     the cutoff update date
     * @return list of updated contract views
     */
    @GetMapping("/client/{clientId}/updated-after")
    public ResponseEntity<List<ContractView>> getContractsByUpdateDate(
            @PathVariable Long clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        var contracts = contractService.getActiveContractsForClient(clientId).stream()
                .filter(c -> c.getUpdateDate() != null && c.getUpdateDate().toLocalDate().isAfter(date))
                .map(ContractView::from)
                .toList();

        return ResponseEntity.ok(contracts);
    }

    /**
     * Updates the cost amount of a specific contract.
     * Automatically updates the "updateDate" to the current timestamp.
     *
     * @param id    the contract ID
     * @param value the new cost amount
     * @return the updated contract view
     */
    @PutMapping("/{id}/amount")
    public ResponseEntity<ContractView> updateContractAmount(@PathVariable Long id, @RequestParam Double value) {
        try {
            Contract updated = contractService.updateContractAmount(id, value);
            return ResponseEntity.ok(ContractView.from(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Calculates the total amount of all active contracts for a specific client.
     *
     * @param clientId the client's ID
     * @return the sum of all active contract costs
     */
    @GetMapping("/client/{clientId}/total")
    public ResponseEntity<Double> getContractsTotal(@PathVariable Long clientId) {
        return ResponseEntity.ok(contractService.getActiveContractsTotal(clientId));
    }
}
