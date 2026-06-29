package com.example.mini_trade_app.order;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.mini_trade_app.order.dto.CreateOrderForm;
import com.example.mini_trade_app.order.dto.OrderDetailsResponse;
import com.example.mini_trade_app.order.dto.OrderItemData;
import com.example.mini_trade_app.order.dto.OrderListingParams;
import com.example.mini_trade_app.order.dto.OrderListingResponse;
import com.example.mini_trade_app.product.Product;
import com.example.mini_trade_app.security.AuthService;
import com.example.mini_trade_app.security.AuthUserInfo;
import com.example.mini_trade_app.security.CustomUserPrincipal;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orders;
    private final AuthService  auth;

    public OrderController(
        AuthService auth,
        OrderService orders
    ) {
        this.auth = auth;
        this.orders = orders;
    }

    @GetMapping("/")
    public OrderListingResponse orderListing(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @RequestBody OrderListingParams param
    ) {
        AuthUserInfo user = auth.verifyUserInfo(principal);
        return orders.getOrderListing(param, user);
    }
    
    @GetMapping("/{orderId}")
    public OrderDetailsResponse getOrderDetails(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @PathVariable Long orderId
    ) {
        AuthUserInfo user = auth.verifyUserInfo(principal);
        return orders.getOrderDetails(orderId, user);
    } // return order details and ref to transactions if completed

    @GetMapping("/product")
    public List<String> getProductListing(
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        auth.verifyUserInfo(principal); // only let valid user to see products listing
        return Arrays.stream(Product.values()).map(Enum::name).toList();
    }

    @PostMapping("/")
    public void createOrder(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @RequestBody CreateOrderForm params
    ) {
        AuthUserInfo user = auth.verifyUserInfo(principal);
        orders.createOrder(params, user);
    }

    @PutMapping("/{orderId}")
    public void checkOutOrder(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @PathVariable Long orderId
    ) {
        AuthUserInfo user = auth.verifyUserInfo(principal);
        orders.checkOutOrder(orderId, user);
    }

    @PostMapping("/{orderId}")
    public void addOrderItem(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @PathVariable Long orderId,
        @RequestBody OrderItemData params
    ) {
        AuthUserInfo user = auth.verifyUserInfo(principal);
        orders.addOrderItem(orderId, params, user);
    }

    @PutMapping("/{orderId}/{orderItemId}")
    public void updateOrderItem(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @PathVariable Long orderId, 
        @PathVariable Long orderItemId,
        @RequestBody OrderItemData params
    ) {
        AuthUserInfo user = auth.verifyUserInfo(principal);
        orders.updateOrderItem(orderId, orderItemId, params, user);
    } // only can update if order is still in draft
    // need to check if allow update items that are not in a transaction

    @DeleteMapping("/{orderId}/{orderIdItem}")
    public void deleteOrderItem(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @PathVariable Long orderId, 
        @PathVariable Long orderItemId
    ) {
        AuthUserInfo user = auth.verifyUserInfo(principal);
        orders.deleteOrderItem(orderId, orderItemId, user);
    }

    @DeleteMapping("/{orderId}")
    public void cancelOrder(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @PathVariable Long orderId
    ) {
        AuthUserInfo user = auth.verifyUserInfo(principal);
        orders.deleteOrder(orderId, user);
    } // cancel any remaining un transaction items canceled
    // trnscation item remain no refund
}
