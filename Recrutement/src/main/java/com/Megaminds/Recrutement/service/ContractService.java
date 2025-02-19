package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.entity.Candidate;
import com.Megaminds.Recrutement.entity.Contract;
import com.Megaminds.Recrutement.repository.CandidateRepository;
import com.Megaminds.Recrutement.repository.ContractRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final CandidateRepository candidateRepository;

    public ContractService(ContractRepository contractRepository, CandidateRepository candidateRepository) {
        this.contractRepository = contractRepository;
        this.candidateRepository = candidateRepository;
    }

    public Contract createContract(Long candidateId, Contract contract) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        contract.setCandidate(candidate);
        return contractRepository.save(contract);
    }

    public Optional<Contract> getContractByCandidate(Long candidateId) {
        return contractRepository.findByCandidateId(candidateId);
    }

    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    public Contract updateContract(Long id, Contract updatedContract) {
        return contractRepository.findById(id).map(contract -> {
            contract.setType(updatedContract.getType());
            contract.setSignedDate(updatedContract.getSignedDate());
            return contractRepository.save(contract);
        }).orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }
}
