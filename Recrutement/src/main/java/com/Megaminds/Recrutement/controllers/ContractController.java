package com.Megaminds.Recrutement.controllers;

import com.Megaminds.Recrutement.entity.Contract;
import com.Megaminds.Recrutement.service.ContractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping("/{candidateId}")
    public ResponseEntity<Contract> createContract(
            @PathVariable Long candidateId,
            @RequestBody Contract contract) {
        Contract newContract = contractService.createContract(candidateId, contract);
        return ResponseEntity.status(HttpStatus.CREATED).body(newContract);
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<Contract> getContractByCandidate(@PathVariable Long candidateId) {
        return contractService.getContractByCandidate(candidateId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable Long id) {
        return contractService.getContractById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(
            @PathVariable Long id,
            @RequestBody Contract updatedContract) {
        return ResponseEntity.ok(contractService.updateContract(id, updatedContract));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
}
