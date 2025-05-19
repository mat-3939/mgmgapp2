package com.example.mgmgapp.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mgmgapp.entity.CartItems;
import com.example.mgmgapp.entity.Products;
import com.example.mgmgapp.repository.user.CartItemRepository;
import com.example.mgmgapp.repository.user.ProductRepository;

@Service
public class CartService {
	
	@Autowired
	private CartItemRepository cartItemRepository;
	

	//カート内の商品を削除
	public void removeItem(int id) {
		//カート内の商品を削除処理
		//RepositoryないのdeleteByIdメソッドを起動
		cartItemRepository.deleteById(id);
	}
	
//	//カート内商品を一覧取得（セッション単位）
//	public List<CartItems> getCartItems(String sessionId){
//		return cartItemRepository.findBySessionId(sessionId);
//	}  ↓修正後
	
	 // カートアイテムと商品情報を取得する
    public List<CartItems> getCartItems(String sessionId) {
        return cartItemRepository.findBySessionIdWithProducts(sessionId); // 修正したメソッドを呼び出す
    }
	
    
    //商品IDから単価を取得する 
    @Autowired
    private ProductRepository productRepository;

//    public int getProductPrice(int productId) {
//    	Integer price = productsRepository.findPriceById(productId);
//    	return (price != null) ? price : 0;
//    }

		
	//カート内の商品数を変更（+、-ボタンの使用）
		public void updateQuantity(int id, int delta) {
			CartItems item = cartItemRepository.findById(id).orElse(null);//リポジトリからidに一致する商品情報を引き出す
			if (item != null) {
		        int newQty = item.getQuantity() + delta; //新しい数を計算
		        if (newQty <= 0) {
		            cartItemRepository.deleteById(id);//もし０以下の数字になったら削除
		        } else {
		            item.setQuantity(newQty);
		            cartItemRepository.save(item);
		        }
		    }
		}
		
		// カートに商品を追加
		public void addToCart(String sessionId, int productId, int quantity) {
		    List<CartItems> existingItems = cartItemRepository.findBySessionId(sessionId).stream()
		        .filter(item -> item.getProduct().getId() == productId)
		        .toList();

		    if (!existingItems.isEmpty()) {
		        CartItems item = existingItems.get(0);
		        item.setQuantity(item.getQuantity() + quantity);
		        cartItemRepository.save(item);
		    } else {
		        Products product = productRepository.findById(productId).orElse(null);
		        if (product != null) {
		            CartItems newItem = new CartItems();
		            newItem.setSessionId(sessionId);
		            newItem.setProduct(product);
		            newItem.setQuantity(quantity);
		            cartItemRepository.save(newItem);
		        }
		    }
		}
		
		// カート内商品の合計個数をカウント
		public int getTotalQuantity(String sessionId) {
		    return cartItemRepository.findBySessionId(sessionId).stream()
		            .mapToInt(CartItems::getQuantity)
		            .sum();
		}

}
