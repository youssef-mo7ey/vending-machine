package com.task.vendingmachine.Repositories;

import com.task.vendingmachine.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Integer> {
}
