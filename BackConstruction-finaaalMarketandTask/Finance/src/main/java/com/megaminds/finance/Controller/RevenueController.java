package com.megaminds.finance.Controller;

import com.megaminds.finance.Entity.ProjectDTO;
import com.megaminds.finance.Entity.Revenue;
import com.megaminds.finance.Service.ProjectClient;
import com.megaminds.finance.Service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("http://localhost:4200")

@RestController
@RequestMapping("/api/revenues")
public class RevenueController {
    @Autowired
    private ProjectClient projectClient;

    @Autowired
    private RevenueService revenueService;

    @GetMapping
    public List<Revenue> getAllRevenues() {
        return revenueService.getAllRevenues();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Revenue> getRevenueById(@PathVariable Long id) {
        Revenue revenue = revenueService.getRevenueById(id);
        return ResponseEntity.ok(revenue);
    }

    @PostMapping
    public Revenue createRevenue(@RequestBody Revenue revenue) {
        return revenueService.createRevenue(revenue);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Revenue> updateRevenue(@PathVariable Long id, @RequestBody Revenue revenueDetails) {
        Revenue updatedRevenue = revenueService.updateRevenue(id, revenueDetails);
        return ResponseEntity.ok(updatedRevenue);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRevenue(@PathVariable Long id) {
        revenueService.deleteRevenue(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/external-projects")
    public ResponseEntity<List<ProjectDTO>> getExternalProjects() {
        return ResponseEntity.ok(projectClient.getAllProjects());
    }

    @GetMapping("/total")
    public Double getTotalRevenue() {
        return revenueService.getTotalRevenue();
    }
    @GetMapping("/average")
    public Double getAverageRevenue() {
        return revenueService.getAverageRevenue();
    }
}