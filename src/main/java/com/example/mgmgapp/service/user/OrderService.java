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

/**
 * 注文処理に関するビジネスロジックを管理するサービスクラス
 * カート内の商品の取得、合計金額の計算、注文処理の実行を担当
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MailService mailService;

    /**
     * 指定されたセッションIDに関連するカート内の商品一覧を取得
     * @param sessionId ユーザーのセッションID
     * @return カート内の商品リスト
     */
    public List<CartItems> getCartItems(String sessionId) {
    	List<CartItems> items = cartItemRepository.findBySessionId(sessionId);
    	System.out.println("取得されたCartItemの数: " + items.size());
        return cartItemRepository.findBySessionId(sessionId);
    }

    /**
     * カート内の商品の合計金額を計算
     * @param sessionId ユーザーのセッションID
     * @return 合計金額
     */
    public int calculateTotal(String sessionId) {
    	// デバッグ用
    	System.out.println("合計計算時のセッションID: " + sessionId);
    	List<CartItems> items = getCartItems(sessionId);
    	System.out.println("カート内商品数: " + items.size());
    	
        return getCartItems(sessionId).stream()
        		.mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    /**
     * 注文処理を実行
     * 注文情報の保存、注文商品の登録、カートのクリア、注文確認メールの送信を行う
     * @param form 注文フォームの情報
     * @param sessionId ユーザーのセッションID
     * @return 保存された注文情報
     */
    @Transactional
    public Orders processOrder(OrderForm form, String sessionId) {
        // 注文情報の作成
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

        // カート内の商品と合計金額を取得
        List<CartItems> cartItems = getCartItems(sessionId);
        int total = calculateTotal(sessionId);
        order.setTotalPrice(total);

        // 注文情報を保存
        Orders savedOrder = orderRepository.save(order);

        // 注文商品の登録
        for (CartItems item : cartItems) {
            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(savedOrder);
            orderItem.setProductName(item.getProduct().getName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getPrice());
            orderItemRepository.save(orderItem);
        }

        // カートのクリアと注文確認メールの送信
        cartItemRepository.deleteBySessionId(sessionId);
        mailService.sendOrderMail(form, cartItems, total);

        return savedOrder;
    }
}
