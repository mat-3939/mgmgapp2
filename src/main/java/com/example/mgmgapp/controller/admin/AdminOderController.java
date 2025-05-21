package com.example.mgmgapp.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mgmgapp.entity.OrderItems;
import com.example.mgmgapp.entity.Orders;
import com.example.mgmgapp.repository.admin.AdminOrderItemRepository;
import com.example.mgmgapp.service.admin.AdminOrderService;

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
    		@RequestParam(required = false) String keyword,
    		@RequestParam(required = false) String startDate,
    		@RequestParam(required = false) String endDate,
    		@RequestParam(required = false) String minAmount,
    		@RequestParam(required = false) String status,
    		@RequestParam(required = false) String sort) {
    	
    	List<Orders> orders = adminOrderService.findAllOrders();
    	
    	// キーワード検索
    	if (keyword != null && !keyword.isEmpty()) {
    		orders = adminOrderService.searchOrders(keyword);
    	}
    	
    	// 日付範囲検索
    	if ((startDate != null && !startDate.isEmpty()) || (endDate != null && !endDate.isEmpty())) {
    		try {
    			orders = adminOrderService.filterOrdersByDateRange(orders, startDate, endDate);
    		} catch (Exception e) {
    			System.err.println("日付フィルタリングエラー: " + e.getMessage());
    		}
    	}
    	
    	// 最低金額検索
    	if (minAmount != null && !minAmount.isEmpty()) {
    		try {
    			int minAmountValue = Integer.parseInt(minAmount);
    			orders = adminOrderService.filterOrdersByMinAmount(orders, minAmountValue);
    		} catch (NumberFormatException e) {
    			System.err.println("金額変換エラー: " + e.getMessage());
    		}
    	}
    	
    	// ステータス検索
    	if (status != null && !status.isEmpty() && !status.equals("all")) {
    		orders = adminOrderService.filterOrdersByStatus(orders, Boolean.parseBoolean(status));
    	}
    	
    	// ソート処理
    	if (sort != null && !sort.isEmpty()) {
    		switch (sort) {
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
    			orders = adminOrderService.sortOrdersByNewest(orders);
    			break;
    		}
    	} else {
    		orders = adminOrderService.sortOrdersByNewest(orders);
    	}
    	
    	// モデルに追加
    	model.addAttribute("orders", orders);
    	model.addAttribute("keyword", keyword);
    	model.addAttribute("startDate", startDate);
    	model.addAttribute("endDate", endDate);
    	model.addAttribute("minAmount", minAmount);
    	model.addAttribute("status", status);
    	model.addAttribute("sort", sort);
    	
    	return "admin/orders";
    }

    /*注文ステータス更新*/
    @PostMapping("/admin/orders/{id}/status")
    public String updateOrderStatus(
    		@PathVariable Integer id, 
    		@RequestParam boolean status,
    		@RequestParam(required = false) String keyword,
    		@RequestParam(required = false) String startDate,
    		@RequestParam(required = false) String endDate,
    		@RequestParam(required = false) String minAmount,
    		@RequestParam(required = false, name = "statusParam") String statusParam,
    		@RequestParam(required = false) String sort,
    		RedirectAttributes redirectAttributes){
    	
        adminOrderService.updateOrderStatus(id, status);
        
        redirectAttributes.addAttribute("keyword", keyword);
        redirectAttributes.addAttribute("startDate", startDate);
        redirectAttributes.addAttribute("endDate", endDate);
        redirectAttributes.addAttribute("minAmount", minAmount);
        redirectAttributes.addAttribute("status", statusParam);
        redirectAttributes.addAttribute("sort", sort);
        
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

}
