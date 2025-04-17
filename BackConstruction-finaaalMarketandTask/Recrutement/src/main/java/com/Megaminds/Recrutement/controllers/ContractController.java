package com.Megaminds.Recrutement.controllers;

import com.Megaminds.Recrutement.entity.Contract;
import com.Megaminds.Recrutement.service.ContractService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ContractController {
    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/generate/{applicationId}")
    public ResponseEntity<byte[]> generateContract(@PathVariable Long applicationId) {
        byte[] contractPdf = contractService.generateContractPdf(applicationId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("contract_" + applicationId + ".pdf")
                .build());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(contractPdf, headers, HttpStatus.OK) ;
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