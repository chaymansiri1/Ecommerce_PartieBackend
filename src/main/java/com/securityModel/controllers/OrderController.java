package com.securityModel.controllers;


import com.securityModel.Dtos.request.OrderRequest;
import com.securityModel.Dtos.response.OrderResponse;
import com.securityModel.models.Order;
import com.securityModel.services.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    public final OrderService orderService ;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/save")
    public OrderResponse addorder(@RequestBody OrderRequest orderRequest){
        return orderService.createorder(orderRequest);
    }
    @PostMapping("/savee/{idClient}")
    public OrderResponse save(@RequestBody OrderRequest orderRequest,@PathVariable Long idClient){
        return orderService.save(orderRequest,idClient);
    }
    @PostMapping("/passerorde/{customerId}/{cartId}")
    public OrderResponse passerorder(@PathVariable Long customerId,@PathVariable Long cartId){
        return orderService.passerOrder(customerId, cartId);
    }




    @GetMapping("/customer/{customerId}")
    public List<Order> getOrdersByCustomerId(@PathVariable Long customerId) {
        return orderService.getOrdersByCustomerId(customerId);
    }


    @GetMapping("/all")
    public List<OrderResponse> allorder(){
        return orderService.allorder();
    }

    @GetMapping("/getbyid/{id}")
    public OrderResponse orderById(@PathVariable Long id){
        return orderService.orderById(id);
    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deleteorder(@PathVariable Long id){

        return orderService.deleteorder(id);
    }


    @PutMapping("/update/{id}")
    public OrderResponse updateorder(@RequestBody OrderRequest orderRequest, @PathVariable Long id){
        return orderService.updateorder(orderRequest,id);
    }


}
