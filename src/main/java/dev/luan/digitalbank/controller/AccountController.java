package dev.luan.digitalbank.controller;

import dev.luan.digitalbank.DTO.AccountDTO;
import dev.luan.digitalbank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO dto) {
        AccountDTO createdAccount = accountService.createAccount(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdAccount.getId()).toUri();
        return ResponseEntity.created(uri).body(createdAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> findById(@PathVariable Long id) {
        AccountDTO accountDTO = accountService.findById(id);
        return ResponseEntity.ok().body(accountDTO);
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<AccountDTO> block(@PathVariable Long id){
        AccountDTO accountDTO = accountService.block(id);
        return ResponseEntity.ok().body(accountDTO);
    }

    @PatchMapping("/{id}/unblock")
    public ResponseEntity<AccountDTO> unblock(@PathVariable Long id){
        AccountDTO accountDTO = accountService.unblock(id);
        return ResponseEntity.ok().body(accountDTO);
    }
    @PatchMapping("/{id}/closed")
    public ResponseEntity<AccountDTO> closed(@PathVariable Long id){
        AccountDTO accountDTO = accountService.closed(id);
        return ResponseEntity.ok().body(accountDTO);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountDTO> deposit(@PathVariable Long id, @RequestBody BigDecimal amount){
        AccountDTO accountDTO = accountService.deposit(id, amount);
        return ResponseEntity.ok().body(accountDTO);
    }


}
