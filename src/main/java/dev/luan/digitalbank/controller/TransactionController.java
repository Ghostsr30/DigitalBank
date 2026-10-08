package dev.luan.digitalbank.controller;

import dev.luan.digitalbank.DTO.AccountDTO;
import dev.luan.digitalbank.DTO.TransactionDTO;
import dev.luan.digitalbank.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}/extract")
    public ResponseEntity<List<TransactionDTO>> extract(@PathVariable Long id){
        List<TransactionDTO> transactions = transactionService.extract(id);
        return ResponseEntity.ok().body(transactions);
    }
}
