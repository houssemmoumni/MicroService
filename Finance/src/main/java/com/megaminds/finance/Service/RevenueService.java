package com.megaminds.finance.Service;

import com.megaminds.finance.Entity.Revenue;
import com.megaminds.finance.Repository.RevenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;

    public List<Revenue> getAllRevenues() {
        return revenueRepository.findAll();
    }

    public Revenue getRevenueById(Long id) {
        return revenueRepository.findById(id).orElseThrow(() -> new RuntimeException("Revenue not found"));
    }

    public Revenue createRevenue(Revenue revenue) {
        return revenueRepository.save(revenue);
    }

    public Revenue updateRevenue(Long id, Revenue revenueDetails) {
        Revenue revenue = revenueRepository.findById(id).orElseThrow(() -> new RuntimeException("Revenue not found"));
        revenue.setDescription_revenue(revenueDetails.getDescription_revenue());
        revenue.setAmount(revenueDetails.getAmount());
        revenue.setDate_revenue(revenueDetails.getDate_revenue());
        revenue.setCategory(revenueDetails.getCategory());
        return revenueRepository.save(revenue);
    }

    public void deleteRevenue(Long id) {
        Revenue revenue = revenueRepository.findById(id).orElseThrow(() -> new RuntimeException("Revenue not found"));
        revenueRepository.delete(revenue);
    }
}