package com.leoprojetos.agregadorinvestimentos.controller;

import com.leoprojetos.agregadorinvestimentos.controller.dto.CreateStockDto;
import com.leoprojetos.agregadorinvestimentos.entity.User;
import com.leoprojetos.agregadorinvestimentos.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/stocks")
@CrossOrigin(origins = "*")  // ← ADICIONA ISSO
public class StockController {

    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<User> createStock(@RequestBody CreateStockDto createStockDto) {
        stockService.createStock(createStockDto);

        return ResponseEntity.ok().build();
    }
}