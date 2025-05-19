package com.example.mgmgapp.service.admin;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mgmgapp.entity.Orders;
import com.example.mgmgapp.repository.admin.AdminOrderRepository;
import com.example.mgmgapp.repository.admin.AdminOrderItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminOrderService {
    /*DI*/
    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderItemRepository adminOrderItemRepository;

    /*注文一覧取得*/
    public List<Orders> findAllOrders() {
        return adminOrderRepository.findAll();
    }

    /*注文詳細取得*/
    public Orders findOrderById(Integer id) {
        return adminOrderRepository.findById(id).orElse(null);
    }

    /*注文ステータスの一覧を取得*/
    public List<Orders> findAllOrdersByStatus(Boolean status) {
        return adminOrderRepository.findByStatus(status);
    }


    /*注文ステータス更新*/
    public void updateOrderStatus(Integer id, Boolean status) {
        Orders order = adminOrderRepository.findById(id).orElse(null);
        order.setStatus(status);
        adminOrderRepository.save(order);
    }

    /*検索条件に基づいて注文を検索*/
    public List<Orders> searchOrders(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return findAllOrders();
        }
        
        // 複数の条件で検索（姓、名、メールアドレス、電話番号など）
        List<Orders> byLastName = adminOrderRepository.findByLastNameContainingOrderByOrderDateDesc(keyword);
        List<Orders> byFirstName = adminOrderRepository.findByFirstNameContainingOrderByOrderDateDesc(keyword);
        List<Orders> byEmail = adminOrderRepository.findByEmailContainingOrderByOrderDateDesc(keyword);
        List<Orders> byTel = adminOrderRepository.findByTelContainingOrderByOrderDateDesc(keyword);
        
        // 結果をマージして重複を排除
        Set<Orders> uniqueOrders = new HashSet<>();
        uniqueOrders.addAll(byLastName);
        uniqueOrders.addAll(byFirstName);
        uniqueOrders.addAll(byEmail);
        uniqueOrders.addAll(byTel);
        
        // リストに変換して日付順(新しい順)にソート
        List<Orders> result = new ArrayList<>(uniqueOrders);
        result.sort(Comparator.comparing(Orders::getOrderDate).reversed());
        
        return result;
    }

    /*詳細検索*/
    public List<Orders> advancedSearch(String lastName, String firstName, 
    LocalDateTime startDate, LocalDateTime endDate,
    BigDecimal minAmount, Boolean status) {
        // 複雑な検索条件の場合は、カスタムリポジトリメソッドを作成するか
        // 複数の条件を組み合わせて検索する
        
        // 例: 日付範囲と金額で検索
        if (startDate != null && endDate != null && minAmount != null) {
            return adminOrderRepository.findByOrderDateBetweenAndTotalPriceGreaterThanOrderByOrderDateDesc(
                startDate, endDate, minAmount);
            }
            
            // 他の条件の組み合わせ...
            
            return findAllOrders();
        }

        /*注文リストをソート（新しい順）*/
        public List<Orders> sortOrdersByNewest(List<Orders> orders) {
            List<Orders> sortedOrders = new ArrayList<>(orders);
            sortedOrders.sort(Comparator.comparing(Orders::getOrderDate).reversed());
            return sortedOrders;
        }
        
        /*注文リストをソート（古い順）*/
        public List<Orders> sortOrdersByOldest(List<Orders> orders) {
            List<Orders> sortedOrders = new ArrayList<>(orders);
            sortedOrders.sort(Comparator.comparing(Orders::getOrderDate));
            return sortedOrders;
        }
        
    /*注文リストを金額でソート（高い順）*/
    public List<Orders> sortOrdersByPriceDesc(List<Orders> orders) {
        List<Orders> sortedOrders = new ArrayList<>(orders);
        sortedOrders.sort(Comparator.comparing(Orders::getTotalPrice).reversed());
        return sortedOrders;
    }

    /*注文リストを金額でソート（安い順）*/
    public List<Orders> sortOrdersByPriceAsc(List<Orders> orders) {
        List<Orders> sortedOrders = new ArrayList<>(orders);
        sortedOrders.sort(Comparator.comparing(Orders::getTotalPrice));
        return sortedOrders;
    }

    /*注文リストをIDでソート（新しい順）*/
    public List<Orders> sortOrdersByIdDesc(List<Orders> orders) {
        List<Orders> sortedOrders = new ArrayList<>(orders);
        sortedOrders.sort(Comparator.comparing(Orders::getId).reversed());
        return sortedOrders;
    }

    /*注文リストをIDでソート（古い順）*/
    public List<Orders> sortOrdersByIdAsc(List<Orders> orders) {
        List<Orders> sortedOrders = new ArrayList<>(orders);
        sortedOrders.sort(Comparator.comparing(Orders::getId));
        return sortedOrders;
    }

    /*注文リストをステータスでフィルタリング*/
    public List<Orders> filterOrdersByStatus(List<Orders> orders, Boolean status) {
        List<Orders> filteredOrders = new ArrayList<>();
        for (Orders order : orders) {
            if (order.getStatus().equals(status)) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }

    /*注文リストを日付範囲でフィルタリング*/
    public List<Orders> filterOrdersByDateRange(List<Orders> orders, String startDate, String endDate) {
        List<Orders> filteredOrders = new ArrayList<>();
        
        // 開始日と終了日を設定
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        
        // デバッグ出力
        System.out.println("フィルタリング開始: startDate=" + startDate);
        System.out.println("フィルタリング終了: endDate=" + endDate);
        
        try {
            if (startDate != null && !startDate.isEmpty()) {
                startDateTime = LocalDateTime.parse(startDate + "T00:00:00");
                System.out.println("開始日設定: " + startDateTime);
            }
            
            if (endDate != null && !endDate.isEmpty()) {
                endDateTime = LocalDateTime.parse(endDate + "T23:59:59");
                System.out.println("終了日設定: " + endDateTime);
            }
        } catch (Exception e) {
            System.err.println("日付パラメータの変換エラー: " + e.getMessage());
            return orders; // エラーの場合は元のリストを返す
        }
        
        // 日付範囲でフィルタリング
        for (Orders order : orders) {
            LocalDateTime orderDate = order.getOrderDate();
            
            boolean includeOrder = true;
            
            if (startDateTime != null && orderDate.isBefore(startDateTime)) {
                includeOrder = false;
            }
            
            if (endDateTime != null && orderDate.isAfter(endDateTime)) {
                includeOrder = false;
            }
            
            if (includeOrder) {
                filteredOrders.add(order);
            }
        }
        
        System.out.println("フィルタリング結果: " + filteredOrders.size() + "件");
        return filteredOrders;
    }

    /**
     * 注文を最低金額でフィルタリングするメソッド
     * @param orders フィルタリングする注文リスト
     * @param minAmount 最低金額
     * @return フィルタリングされた注文リスト
     */
    public List<Orders> filterOrdersByMinAmount(List<Orders> orders, int minAmount) {
        return orders.stream()
            .filter(order -> order.getTotalPrice() >= minAmount)
            .collect(Collectors.toList());
    }

    public int getWeeklySalesAmount() {
        return adminOrderItemRepository.findAll().stream()
            .filter(orderItem -> orderItem.getOrder().getOrderDate().toLocalDate().isAfter(LocalDate.now().minusDays(7)))
            .mapToInt(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
            .sum();
    }

}
