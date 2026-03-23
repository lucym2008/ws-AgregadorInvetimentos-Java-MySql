package com.leoprojetos.agregadorinvestimentos.repository;


import com.leoprojetos.agregadorinvestimentos.entity.AccountStock;
import com.leoprojetos.agregadorinvestimentos.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}