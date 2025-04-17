package com.megaminds.material.repository;

import com.megaminds.material.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material,Integer> {
    List<Material> findAllByIdInOrderById(List<Integer> ids);
}
