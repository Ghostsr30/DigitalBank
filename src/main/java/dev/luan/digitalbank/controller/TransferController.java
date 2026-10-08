package dev.luan.digitalbank.controller;

import dev.luan.digitalbank.DTO.AccountDTO;
import dev.luan.digitalbank.DTO.TransactionDTO;
import dev.luan.digitalbank.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/{sourceId}/{targetId}")
    public ResponseEntity<Void> transfer(@PathVariable Long sourceId, @PathVariable Long targetId, @RequestBody BigDecimal amount) {
        transferService.transfer(sourceId, targetId, amount);
        return ResponseEntity.noContent().build();
    }
}
