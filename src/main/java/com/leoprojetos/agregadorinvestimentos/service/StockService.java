package com.leoprojetos.agregadorinvestimentos.service;


import com.leoprojetos.agregadorinvestimentos.controller.dto.CreateStockDto;
import com.leoprojetos.agregadorinvestimentos.entity.Stock;
import com.leoprojetos.agregadorinvestimentos.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    public void createStock(CreateStockDto createStockDto) {

        var stock = new Stock(
                createStockDto.stockId(),
                createStockDto.description()
        );

        stockRepository.save(stock);
    }
}
