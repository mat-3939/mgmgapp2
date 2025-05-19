package com.example.mgmgapp.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.mgmgapp.entity.CartItems;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Integer>{
	
	List<CartItems> findBySessionId(String sessionId);
//	List<CartItems> findByPrice(int price);
	List<CartItems> findByQuantity(int quantity);
	
	// セッションIDに基づいてカートアイテムを取得し、関連する商品情報も一緒に取得
    @Query("SELECT c FROM CartItems c JOIN FETCH c.product WHERE c.sessionId = :sessionId ORDER BY c.id ASC")
    List<CartItems> findBySessionIdWithProducts(@Param("sessionId") String sessionId);
    
    void deleteBySessionId(String sessionId);
    
}
