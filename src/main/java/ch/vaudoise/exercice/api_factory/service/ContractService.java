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

    public Contract saveContract(Contract contract) {
        if (contract.getStartDate() == null) {
            contract.setStartDate(LocalDate.now());
        }
        return contractRepository.save(contract);
    }

    public List<Contract> getActiveContractsForClient(Long clientId) {
        return contractRepository.findByClientIdAndEndDateAfterOrEndDateIsNull(
                clientId, LocalDate.now(), clientId
        );
    }

    public List<Contract> getAllContractsForClient(Long clientId) {
        return contractRepository.findByClientId(clientId);
    }

    public Contract updateContractAmount(Long contractId, Double newAmount) {
        return contractRepository.findById(contractId).map(contract -> {
            contract.setCostAmount(newAmount);
            contract.setUpdateDate(java.time.LocalDateTime.now());
            return contractRepository.save(contract);
        }).orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    public Double getActiveContractsTotal(Long clientId) {
        return getActiveContractsForClient(clientId).stream()
                .mapToDouble(Contract::getCostAmount)
                .sum();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

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
