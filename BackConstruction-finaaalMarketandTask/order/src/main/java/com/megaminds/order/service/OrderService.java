package com.megaminds.order.service;

import com.megaminds.order.dto.OrderLineRequest;
import com.megaminds.order.dto.OrderRequest;
import com.megaminds.order.dto.OrderResponse;
import com.megaminds.order.entity.Order;
import com.megaminds.order.entity.PaymentMethod;
import com.megaminds.order.entity.User;
import com.megaminds.order.kafka.OrderConfirmation;
import com.megaminds.order.kafka.OrderProducer;
import com.megaminds.order.material.MaterialClient;
import com.megaminds.order.material.PurchaseRequest;
import com.megaminds.order.material.PurchaseResponse;
import com.megaminds.order.repository.OrderRepository;
import com.megaminds.order.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final MaterialClient materialClient;
    private final SmsService smsService;


    @Value("${application.static-user-id}") // Static user ID from configuration
    private Integer staticUserId;

    @Transactional
    public Integer createOrder(OrderRequest request) {
        var purchasedProducts = materialClient.purchaseProducts(request.materials());
        var order = this.orderRepository.save(mapper.toOrder(request));
        System.out.println(request.materials());
        for (PurchaseRequest purchaseRequest : request.materials()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            order.getId(),
                            purchaseRequest.materialId(),
                            purchaseRequest.quantity()
                    )
            );
        }
        /*
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        */

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        order.getReference(),
                        request.amount(),
                        request.paymentMethod(),
                        purchasedProducts
                )
        );
        // Envoi du SMS de confirmation
            String message = String.format("Votre commande %s a été confirmée avec un montant de %.2f.",
                    order.getReference(), request.amount());
            smsService.sendSms("+21695359282", message); // Remplacez par le numéro du client

        return order.getId();
    }

    public List<OrderResponse> findAllOrders() {
        return this.orderRepository.findAll()
                .stream()
                .map(this.mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer id) {
        return this.orderRepository.findById(id)
                .map(this.mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", id)));
    }

}
