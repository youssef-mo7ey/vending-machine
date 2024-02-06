package com.task.vendingmachine.Service;

import com.task.vendingmachine.Configuration.JwtService;
import com.task.vendingmachine.Models.DTO.BuyDTO;
import com.task.vendingmachine.Models.Product;
import com.task.vendingmachine.Models.DTO.ProductDTO;
import com.task.vendingmachine.Models.User;
import com.task.vendingmachine.Repositories.ProductRepo;
import com.task.vendingmachine.Repositories.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final JwtService jwtService;
    private final UserRepo userRepo;

    //Get All
    public List<Product> getAll(){
        return productRepo.findAll();
    }
    //Buy
    public String buyCertainProduct (HttpServletRequest req, BuyDTO buyDTO){
        String token=req.getHeader("Authorization").substring(7);
        User user=jwtService.getUserFromToken(token);
        Product product=productRepo.findById(buyDTO.getId()).orElseThrow();
        int total=product.getPrice()* buyDTO.getAmount();
        if (user.getDeposit()-total < 0  || product.getAmount()-buyDTO.getAmount()<=0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please Enter The Right amount or more and make sure the item exist"
            );
        }

        product.setAmount(product.getAmount()-buyDTO.getAmount());
        int change=user.getDeposit()-total;
        user.setDeposit(null);
        userRepo.save(user);
        productRepo.save(product);
        if (change>0){
            return "The Purchase of " + product.getName() + " done successfully and your total is:" +total +", and you got change of " + change;
        }
        return "The Purchase of " + product.getName() + " done successfully and your total is:" + total ;


    }
    //reset
    public void resetDeposit(HttpServletRequest req){
        String token=req.getHeader("Authorization").substring(7);
        User user=jwtService.getUserFromToken(token);
        user.setDeposit(null);
        userRepo.save(user);
    }
    //Post
    public Product postNewProduct(HttpServletRequest req, ProductDTO prod){
        String token =req.getHeader("Authorization").substring(7);
        String username= jwtService.getUsernameFromToken(token);
        Optional<User> user=userRepo.findByUsername(username);
        Product product=Product.builder()
                            .name(prod.getName())
                            .amount(prod.getAmount())
                            .price(prod.getPrice())
                            .user(user.orElse(new User()))
                            .build();
        return productRepo.save(product);
    }
    //delete
    public void deleteProduct(HttpServletRequest req,Integer id){
        String token = req.getHeader("Authorization").substring(7);
        User user=jwtService.getUserFromToken(token);
        Product product = productRepo.findById(id).orElseThrow();

        if(!Objects.equals(user.getId(), product.getUser().getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"This user is forbidden");
        }
        productRepo.deleteById(id);
    }
    //update
    public Product updateProduct(HttpServletRequest req,ProductDTO productDTO,Integer productId){
        String token=req.getHeader("Authorization").substring(7);
        Product product = productRepo.findById(productId).orElseThrow();
        User user = jwtService.getUserFromToken(token);

        if (!Objects.equals(product.getUser().getId(), user.getId())){
            return null;
        }
        if (productDTO.getPrice() != null) {
            product.setPrice(productDTO.getPrice());
        }
        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getAmount() != null) {
                    product.setAmount(productDTO.getAmount());
                }

        return productRepo.save(product);
    }
}
