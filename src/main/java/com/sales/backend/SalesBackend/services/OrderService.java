package com.sales.backend.SalesBackend.services;

import com.sales.backend.SalesBackend.dtos.CreateOrderRequest;
import com.sales.backend.SalesBackend.dtos.OrderDto;

import com.sales.backend.SalesBackend.dtos.OrderUpdateRequest;
import com.sales.backend.SalesBackend.dtos.PageableResponse;

import java.util.List;

public interface OrderService {

    //create order
    OrderDto createOrder(CreateOrderRequest orderDto);

    //remove order
    void removeOrder(String orderId);

    //get orders of user
    List<OrderDto> getOrdersOfUser(String userId);

    //get orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    OrderDto updateOrder(String orderId, OrderUpdateRequest request);

    //order methods(logic) related to order

}
