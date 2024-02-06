package com.task.vendingmachine.Controller;

import com.task.vendingmachine.Models.DTO.BuyDTO;
import com.task.vendingmachine.Models.Product;
import com.task.vendingmachine.Models.DTO.ProductDTO;
import com.task.vendingmachine.Service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping("/")
    public List<Product> getAll(){
        return productService.getAll();
    }

    @GetMapping("/buy")
    public ResponseEntity<String> getProductById(HttpServletRequest req, @RequestBody BuyDTO buyDTO){
        String message= productService.buyCertainProduct(req,buyDTO);
        if(message == null){
            return ResponseEntity.badRequest().body("Something went wrong!!!");
        }
        else{
            return ResponseEntity.ok(message);
        }
    }

    @GetMapping("/reset")
    public ResponseEntity<String> reset(HttpServletRequest req){
        productService.resetDeposit(req);
        return ResponseEntity.ok("Deposit reset");
    }
    @PostMapping
    public Product postProduct(HttpServletRequest req, @RequestBody ProductDTO product){
        return productService.postNewProduct(req, product);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> postProduct(HttpServletRequest req,@PathVariable Integer id){
        productService.deleteProduct(req, id);
        return ResponseEntity.ok("The Product With id:"+ id +" has been deleted");
    }
    @PatchMapping("/{id}")
        public Product patchProduct(
                HttpServletRequest req,
                @RequestBody ProductDTO product,
                @PathVariable Integer id){
            return productService.updateProduct(req, product,id);
        }
}
