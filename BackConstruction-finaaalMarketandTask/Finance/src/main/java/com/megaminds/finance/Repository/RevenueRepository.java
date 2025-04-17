package com.megaminds.finance.Repository;

import com.megaminds.finance.Entity.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RevenueRepository extends JpaRepository<Revenue, Long> {

    @Query("SELECT SUM(r.amount) FROM Revenue r")
    Double getTotalRevenue();
    @Query("SELECT AVG(r.amount) FROM Revenue r")
    Double getAverageRevenue();
}
