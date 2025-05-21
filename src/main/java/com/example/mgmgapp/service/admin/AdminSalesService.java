package com.example.mgmgapp.service.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.mgmgapp.repository.admin.AdminOrderItemRepository;
import com.example.mgmgapp.repository.admin.AdminOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminSalesService {
    /*DI*/
    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderItemRepository adminOrderItemRepository;
    
    //次回ここから
    /*注文件数の合計を取得*/
    public int getOrderCount() {
        return (int) adminOrderRepository.count();
    }

    /*注文商品の合計数を取得（各注文の商品数を合計）*/
    public int getTotalItemCount() {
        return adminOrderItemRepository.findAll().size();
    }

    /*注文ごとの平均商品数を取得*/
    public double getAverageItemsPerOrder() {
        int orderCount = getOrderCount();
        if (orderCount == 0) {
            return 0;
        }
        int totalItems = getTotalItemCount();
        return (double) totalItems / orderCount;
    }

    /*注文数の対応済みの合計を取得*/
    public int getCompletedOrderCount() {
        return adminOrderRepository.findAllByStatus(true).size();
    }

    /*注文数の未対応の合計を取得*/
    public int getPendingOrderCount() {
        return adminOrderRepository.findAllByStatus(false).size();
    }

    /*本日の売上金額を取得*/
    public int getTodaySalesAmount() {
        Integer amount = adminOrderItemRepository.findTodaySalesAmount();
        return amount != null ? amount : 0;
    }

    /**
     * 本日の注文数を取得するメソッド
     * @return 本日の注文数
     */
    public long getTodayOrderCount() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        return adminOrderRepository.countByOrderDateBetween(startOfDay, endOfDay);
    }
    
    /* 週間の売上金額を取得（status=true） */
    public int getWeeklySalesAmount() {
        return adminOrderItemRepository.findAll().stream()
            .filter(orderItem -> {
                var order = orderItem.getOrder();
                return Boolean.TRUE.equals(order.getStatus()) &&
                       order.getOrderDate().toLocalDate().isAfter(LocalDate.now().minusDays(7));
            })
            .mapToInt(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
            .sum();
    }
    
    /* 月間の売上金額を取得（status=true） */
    public int getMonthlySalesAmount() {
    	return adminOrderItemRepository.findAll().stream()
    			.filter(orderItem -> {
    				var order = orderItem.getOrder();
    				return Boolean.TRUE.equals(order.getStatus()) &&
    						order.getOrderDate().toLocalDate().isAfter(LocalDate.now().minusMonths(1));
    			})
    			.mapToInt(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
    			.sum();
    }
    
    /* 年間の売上金額を取得（status=true） */
    public int getYearlySalesAmount() {
        return adminOrderItemRepository.findAll().stream()
            .filter(orderItem -> {
                var order = orderItem.getOrder();
                return Boolean.TRUE.equals(order.getStatus()) &&
                       order.getOrderDate().toLocalDate().isAfter(LocalDate.now().minusYears(1));
            })
            .mapToInt(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
            .sum();
    }

    /*週間の売上件数を取得*/
    public int getWeeklySalesCount() {
        return (int) adminOrderRepository.findAll().stream()
        		.filter(order -> order.getOrderDate().toLocalDate().isAfter(LocalDate.now().minusDays(7)))
                .count();
    }

    /*月間の売上件数を取得*/
    public int getMonthlySalesCount() {
        return (int) adminOrderRepository.findAll().stream()
        		.filter(order -> order.getOrderDate().toLocalDate().isAfter(LocalDate.now().minusMonths(1)))
            .count();
    }

    /*年間の売上金額を取得*/
    public int getYearlySalesCount() {
        return (int) adminOrderRepository.findAll().stream()
        		.filter(order -> order.getOrderDate().toLocalDate().isAfter(LocalDate.now().minusYears(1)))
            .count();
    }    
    
}
