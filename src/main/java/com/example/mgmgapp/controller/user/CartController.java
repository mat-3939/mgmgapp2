package com.example.mgmgapp.controller.user;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mgmgapp.entity.CartItems;
import com.example.mgmgapp.service.user.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	
	@GetMapping
public String cart(Model model, HttpSession session) {

		 // テスト用に仮のセッションIDをセット
		// 仮のセッションID
//	    String sessionId = "sessionId1";
		//	    // セッションIDを取得
	    String sessionId = session.getId();

	    // DBからカートアイテム一覧を取得
	    List<CartItems> cartItems = cartService.getCartItems(sessionId);
	    model.addAttribute("cartItems", cartItems);

	    System.out.println("cartItems is null? " + (cartItems == null));
	    System.out.println("cartItems size: " + (cartItems != null ? cartItems.size() : "null"));

	    //★★★★★
	    // 合計金額を計算
	    
//	    int total = 0;
//	    for (CartItems item : cartItems) {
//	        int productId = item.getProduct().getId(); // product_id を取得
//	        int price = cartService.getProductPrice(productId); // 単価を取得
//	        total += price * item.getQuantity(); // 小計を足す
//	        
//	        System.out.println("〇〇〇");
//	        System.out.println(item.getProduct());
//	        System.out.println("〇〇〇");
//	    }
	    
	    System.out.println("Cart Items: " + cartItems);
	    System.out.println("Session ID: " + sessionId);
	    
	    int uniqueItemCount = (int) cartItems.stream()
	    		.filter(item -> sessionId != null && sessionId.equals(item.getSessionId()))  // セッションIDでフィルタリング
	    		.map(item -> item.getProduct().getName() != null ? item.getProduct().getName() : "Unknown Product")  // 商品名を取得
	    		.distinct()  // 重複を除く
	    		.count();  // ユニークな商品の数をカウント（int型にキャスト）

	    model.addAttribute("uniqueItemCount", uniqueItemCount);
	    
	    int subtotal = cartItems.stream()
	    		.mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
	    		.sum();
	    model.addAttribute("subtotal", subtotal);

	    int total = cartItems.stream()
	    		.mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
	    		.sum();
	    model.addAttribute("total", total);
	    
	    System.out.println("★★★");
	    System.out.println(total);
	    System.out.println("★★★");
	    
	    return "user/cart";
	}
	
	//削除用コントローラ
	@PostMapping("/remove/{id}")
	public String removeItemFromCart(@PathVariable("id") int id){
		//カートから指定された商品を削除（サービスを動かす）
		cartService.removeItem(id);
		return "redirect:/cart";
	}
	
	//数量変更用コントローラ(おそらく+、-ボタンで数量を変更して、確定ボタンを押したとき）
	@PostMapping("/update/{id}")
	public String updateItemQuantity(
		@PathVariable("id") int id, int delta //フォームから「+1」or「-1」が来る想定
		) {
		cartService.updateQuantity(id,delta); //idと変更数（+-1）を送る
		return "redirect:/cart"; //画面を更新
	}
	
	// カートに商品を追加
	@PostMapping("/add")
	public String addToCart(
	        @RequestParam("productId") int productId,
	        @RequestParam("quantity") int quantity,
	        HttpSession session) {

	    // 本番ではセッションから取得
//	    String sessionId = "sessionId1";
	    String sessionId = session.getId();
	    
	    cartService.addToCart(sessionId, productId, quantity);
	    System.out.println("カート追加時のセッションID: " + session.getId());

	    return "redirect:/cart";
	}
	
}
