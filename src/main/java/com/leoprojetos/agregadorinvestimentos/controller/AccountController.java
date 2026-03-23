package com.leoprojetos.agregadorinvestimentos.controller;

import com.leoprojetos.agregadorinvestimentos.controller.dto.AccountStockDto;
import com.leoprojetos.agregadorinvestimentos.controller.dto.AccountStockResponseDto;
import com.leoprojetos.agregadorinvestimentos.entity.User;
import com.leoprojetos.agregadorinvestimentos.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stocks")
    @CrossOrigin(origins = "*")  // ← ADICIONA ISSO
    public ResponseEntity<User> associateStock(@PathVariable("accountId") String accountId,
                                               @RequestBody AccountStockDto accountStockDto) {

        accountService.associateStock(accountId, accountStockDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDto>> associateStock(@PathVariable("accountId") String accountId) {

        var stocks = accountService.listStocks(accountId);

        return ResponseEntity.ok(stocks);
    }
}