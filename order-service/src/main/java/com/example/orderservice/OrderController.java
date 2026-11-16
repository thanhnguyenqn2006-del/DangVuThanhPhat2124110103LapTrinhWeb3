
package com.example.orderservice;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final RestClient restClient;

    public OrderController(RestClient.Builder builder) {
        this.restClient = builder
            .baseUrl("http://localhost:8081")
            .build();
    }

    @GetMapping
    public List<Map<String, Object>> getOrders() {
        return List.of(
            Map.of("id", 101, "productId", 1, "quantity", 2),
            Map.of("id", 102, "productId", 2, "quantity", 1)
        );
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable int id) {
        return restClient.get()
            .uri("/api/products/{id}", id)
            .retrieve()
            .body(String.class);
    }

    // Bài tập mở rộng: Lấy thông tin đơn hàng chi tiết kèm thông tin sản phẩm và tính tổng tiền
    @GetMapping("/{id}")
    public Map<String, Object> getOrderWithProductDetails(@PathVariable int id) {
        List<Map<String, Object>> orders = getOrders();
        Map<String, Object> order = orders.stream()
            .filter(o -> o.get("id").equals(id))
            .findFirst()
            .orElse(null);

        if (order == null) {
            return Map.of("error", "Order not found with id: " + id);
        }

        int productId = (int) order.get("productId");
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> product = restClient.get()
                .uri("/api/products/{id}", productId)
                .retrieve()
                .body(Map.class);

            Map<String, Object> result = new LinkedHashMap<>(order);
            result.put("product", product);
            if (product != null && product.containsKey("price")) {
                double price = Double.parseDouble(product.get("price").toString());
                int quantity = (int) order.get("quantity");
                result.put("totalPrice", price * quantity);
            }
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new LinkedHashMap<>(order);
            result.put("productError", "Cannot fetch product from Product Service: " + e.getMessage());
            return result;
        }
    }
}

