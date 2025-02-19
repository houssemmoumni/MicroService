package com.Megaminds.Recrutement.repository;

import com.Megaminds.Recrutement.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    Optional<Contract> findByCandidateId(Long candidateId);
}
