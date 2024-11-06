package com.securityModel.services;



import com.securityModel.Dtos.request.OrderRequest;
import com.securityModel.Dtos.response.OrderResponse;
import com.securityModel.models.Order;

import java.util.HashMap;
import java.util.List;

public interface OrderService {
    OrderResponse createorder(OrderRequest order);
    OrderResponse save(OrderRequest orderRequest,Long clientId);

    List<OrderResponse> allorder();
    OrderResponse orderById(Long id);
    OrderResponse updateorder (OrderRequest orderRequest, Long id);
    HashMap<String,String> deleteorder(Long id);

    OrderResponse passerOrder(Long customerId, Long cartId);


    public List<Order> getOrdersByCustomerId(Long customerId);

}
