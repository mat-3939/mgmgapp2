package com.example.mgmgapp.service.user;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.mgmgapp.entity.CartItems;
import com.example.mgmgapp.entity.OrderItems;
import com.example.mgmgapp.entity.Orders;
import com.example.mgmgapp.form.OrderForm;
import com.example.mgmgapp.repository.user.CartItemRepository;
import com.example.mgmgapp.repository.user.OrderItemRepository;
import com.example.mgmgapp.repository.user.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MailService mailService;

    public List<CartItems> getCartItems(String sessionId) {
    	List<CartItems> items = cartItemRepository.findBySessionId(sessionId);
    	System.out.println("取得されたCartItemの数: " + items.size());
        return cartItemRepository.findBySessionId(sessionId);
    }

    public int calculateTotal(String sessionId) {
    	// デバッグ用
    	System.out.println("合計計算時のセッションID: " + sessionId);
    	List<CartItems> items = getCartItems(sessionId);
    	System.out.println("カート内商品数: " + items.size());
    	
        return getCartItems(sessionId).stream()
        		.mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    @Transactional
    public Orders processOrder(OrderForm form, String sessionId) {
        Orders order = new Orders();
        order.setEmail(form.getEmail());
        order.setFirstName(form.getFirstName());
        order.setLastName(form.getLastName());
        order.setPostcode(form.getPostcode());
        order.setPrefecture(form.getPrefecture());
        order.setCity(form.getCity());
        order.setAddressLine(form.getAddressLine());
        order.setBuilding(form.getBuilding());
        order.setTel(form.getTel());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(false);

        List<CartItems> cartItems = getCartItems(sessionId);
        int total = calculateTotal(sessionId);
        order.setTotalPrice(total);

        Orders savedOrder = orderRepository.save(order);

        for (CartItems item : cartItems) {
            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(savedOrder);
            orderItem.setProductName(item.getProduct().getName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getPrice());
            orderItemRepository.save(orderItem);
        }

        cartItemRepository.deleteBySessionId(sessionId);
        mailService.sendOrderMail(form, cartItems, total);

        return savedOrder;
    }
}
