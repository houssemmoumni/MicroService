package com.megaminds.finance.Controller;

import com.megaminds.finance.Entity.Expense;
import com.megaminds.finance.Service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // Récupérer tous les expenses
    @GetMapping
    public List<Expense> getAllExpenses(Authentication authentication) {
        String token = getTokenFromAuthentication(authentication);
        // Tu peux maintenant utiliser ce token si tu en as besoin pour des appels externes ou autre logique
        System.out.println("Token JWT : " + token);
        return expenseService.getAllExpenses();
    }

    // Récupérer un expense par ID
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id, Authentication authentication) {
        String token = getTokenFromAuthentication(authentication);
        System.out.println("Token JWT : " + token);
        Expense expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expense);
    }

    // Créer un expense
    @PostMapping
    public Expense createExpense(@RequestBody Expense expense, Authentication authentication) {
        String token = getTokenFromAuthentication(authentication);
        System.out.println("Token JWT : " + token);
        return expenseService.createExpense(expense);
    }

    // Mettre à jour un expense
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense expenseDetails, Authentication authentication) {
        String token = getTokenFromAuthentication(authentication);
        System.out.println("Token JWT : " + token);
        Expense updatedExpense = expenseService.updateExpense(id, expenseDetails);
        return ResponseEntity.ok(updatedExpense);
    }

    // Supprimer un expense
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id, Authentication authentication) {
        String token = getTokenFromAuthentication(authentication);
        System.out.println("Token JWT : " + token);
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    // Récupérer le total des dépenses
    @GetMapping("/total")
    public Double getTotalExpense(Authentication authentication) {
        String token = getTokenFromAuthentication(authentication);
        System.out.println("Token JWT : " + token);
        return expenseService.getTotalExpense();
    }

    // Méthode utilitaire pour récupérer le token de l'Authentication
    private String getTokenFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getTokenValue();
        }
        return null;
    }
}