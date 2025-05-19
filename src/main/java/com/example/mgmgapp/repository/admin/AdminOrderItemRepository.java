package com.example.mgmgapp.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.mgmgapp.entity.OrderItems;

@Repository
public interface AdminOrderItemRepository extends JpaRepository<OrderItems, Integer> {
    /*同じorder_idは1件として取得*/
    @Query("SELECT COUNT(DISTINCT oi.order.id) FROM OrderItems oi")
    int countDistinctOrderCount();
    
    /*重複していないorder_idを取得*/
    @Query("SELECT DISTINCT oi.order.id FROM OrderItems oi")
    List<Integer> findAllDistinctOrderIds();
    
    /*注文IDに基づいて注文商品を取得*/
    List<OrderItems> findByOrderId(Integer orderId);

    @Query("SELECT SUM(oi.price * oi.quantity) FROM OrderItems oi WHERE FUNCTION('DATE', oi.order.orderDate) = FUNCTION('CURRENT_DATE') AND oi.order.status = true")
    Integer findTodaySalesAmount();

    @Query("SELECT COUNT(oi) FROM OrderItems oi WHERE FUNCTION('DATE', oi.order.orderDate) = FUNCTION('CURRENT_DATE')")
    int countTodayOrderItems();
}
