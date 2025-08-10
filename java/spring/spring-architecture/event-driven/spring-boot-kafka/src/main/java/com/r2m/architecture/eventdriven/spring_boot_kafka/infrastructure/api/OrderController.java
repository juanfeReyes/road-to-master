package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.api;

import com.r2m.architecture.eventdriven.spring_boot_kafka.application.CreateOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderService createOrderService;

    @Autowired
    public OrderController(CreateOrderService createOrderService) {
        this.createOrderService = createOrderService;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendOrder(@RequestBody String message) throws ExecutionException, InterruptedException, TimeoutException {
        createOrderService.execute(message);
    }
}
