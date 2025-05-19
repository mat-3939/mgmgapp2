package com.example.mgmgapp.controller.admin;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mgmgapp.entity.Orders;
import com.example.mgmgapp.service.admin.AdminOrderService;
import com.example.mgmgapp.entity.OrderItems;
import com.example.mgmgapp.repository.admin.AdminOrderItemRepository;

import lombok.RequiredArgsConstructor;
/*ページURL(一覧):http://localhost:8080/admin/orders */
/*ページURL(詳細):http://localhost:8080/admin/order/{id} */
@Controller
@RequiredArgsConstructor
public class AdminOderController 
{
    /*DI*/
    private final AdminOrderService adminOrderService;
    private final AdminOrderItemRepository adminOrderItemRepository;

    /*注文一覧ページ*/
    @GetMapping("/admin/orders")
    public String showOrders(Model model, 
                            @RequestParam(required = false) String sort,
                            @RequestParam(required = false) String status,
                            @RequestParam(required = false) String keyword) 
    {
        /*注文一覧データ取得*/
        List<Orders> orders = adminOrderService.findAllOrders();

        /*注文一覧データから検索*/
        if (keyword != null && !keyword.isEmpty()) {
            orders = adminOrderService.searchOrders(keyword);
        }
        
        /*ステータスに基づいて絞り込み(未対応/対応済み ＋ すべて)*/
        if (status != null && !status.isEmpty()) 
        {
            if (status.equals("all")) {
                // すでに全件取得済みか検索済みなので何もしない
            } else {
                orders = adminOrderService.filterOrdersByStatus(orders, Boolean.parseBoolean(status));
            }
        }
        
        /*ソートパラメータに基づいて並び替え*/
        if (sort != null && !sort.isEmpty()) 
        {
            switch (sort) 
            {
                case "new":
                    orders = adminOrderService.sortOrdersByNewest(orders);
                    break;
                case "old":
                    orders = adminOrderService.sortOrdersByOldest(orders);
                    break;
                case "price_high":
                    orders = adminOrderService.sortOrdersByPriceDesc(orders);
                    break;
                case "price_low":
                    orders = adminOrderService.sortOrdersByPriceAsc(orders);
                    break;
                case "id_new":
                    orders = adminOrderService.sortOrdersByIdDesc(orders);
                    break;
                case "id_old":
                    orders = adminOrderService.sortOrdersByIdAsc(orders);
                    break;
                default:
                    /*デフォルトは新しい順*/
                    orders = adminOrderService.sortOrdersByNewest(orders);
                    break;
            }
        } else {
            // デフォルトのソート（新しい順）
            orders = adminOrderService.sortOrdersByNewest(orders);
        }
        
        /*注文一覧データをモデルに追加*/
        model.addAttribute("orders", orders);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("sort", sort);
        
        return "admin/orders";
    }

    /*注文ステータス更新*/
    @PostMapping("/admin/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Integer id, @RequestParam Boolean status) 
    {
        adminOrderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders";
    }
    
    /*注文詳細ページからのステータス更新*/
    @PostMapping("/admin/orders/{id}/status/detail")
    public String updateOrderStatusFromDetail(@PathVariable Integer id, @RequestParam Boolean status) 
    {
        adminOrderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders/" + id;
    }

    /*注文詳細ページ*/
    @GetMapping("/admin/orders/{id}")
    public String showOrderDetail(@PathVariable Integer id, Model model) 
    {
        /*注文情報取得*/
        Orders order = adminOrderService.findOrderById(id);
        model.addAttribute("order", order);
        
        /*注文商品情報取得*/
        List<OrderItems> orderItems = adminOrderItemRepository.findByOrderId(id);
        model.addAttribute("orderItems", orderItems);
        
        /*注文詳細ページ表示*/
        return "admin/order_detail";
    }
    
    /*検索用エンドポイントを追加*/
    @GetMapping("/admin/orders/search")
    public String searchOrders(Model model,
                              @RequestParam(required = false) String keyword,
                              @RequestParam(required = false) String startDate,
                              @RequestParam(required = false) String endDate,
                              @RequestParam(required = false) String minAmount) 
    {
        List<Orders> orders;
        
        // キーワード検索
        if (keyword != null && !keyword.isEmpty()) {
            orders = adminOrderService.searchOrders(keyword);
        } else {
            orders = adminOrderService.findAllOrders();
        }
        
        // 日付範囲での検索
        boolean hasDateFilter = false;
        System.out.println("日付パラメータ: startDate=" + startDate + ", endDate=" + endDate);
        if ((startDate != null && !startDate.isEmpty()) || (endDate != null && !endDate.isEmpty())) {
            try {
                orders = adminOrderService.filterOrdersByDateRange(orders, startDate, endDate);
                hasDateFilter = true;
            } catch (Exception e) {
                // ログに記録
                System.err.println("日付フィルタリングエラー: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // 最低金額での検索
        boolean hasAmountFilter = false;
        if (minAmount != null && !minAmount.isEmpty()) {
            try {
                int minAmountValue = Integer.parseInt(minAmount);
                orders = adminOrderService.filterOrdersByMinAmount(orders, minAmountValue);
                hasAmountFilter = true;
            } catch (NumberFormatException e) {
                // ログに記録
                System.err.println("金額変換エラー: " + e.getMessage());
            }
        }
        
        // デバッグ情報
        System.out.println("検索結果件数: " + orders.size());
        System.out.println("日付フィルタ適用: " + hasDateFilter);
        System.out.println("金額フィルタ適用: " + hasAmountFilter);
        
        // 詳細検索パラメータをモデルに追加
        model.addAttribute("keyword", keyword);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("minAmount", minAmount);
        
        // 検索結果をモデルに追加
        model.addAttribute("orders", orders);
        
        return "admin/orders";
    }
}
