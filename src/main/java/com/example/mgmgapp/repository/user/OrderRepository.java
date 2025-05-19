package com.example.mgmgapp.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mgmgapp.entity.Orders;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
	
}
