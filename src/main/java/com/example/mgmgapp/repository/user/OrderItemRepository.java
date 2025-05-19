package com.example.mgmgapp.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mgmgapp.entity.OrderItems;

public interface OrderItemRepository extends JpaRepository<OrderItems, Integer> {
	
}
