package com.Megaminds.Recrutement.repository;

import com.Megaminds.Recrutement.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
