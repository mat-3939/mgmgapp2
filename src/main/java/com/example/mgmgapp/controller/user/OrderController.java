package com.example.mgmgapp.controller.user;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mgmgapp.form.OrderForm;
import com.example.mgmgapp.service.user.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 注文入力フォームの表示
    @GetMapping("/form")
    public String showOrderForm(Model model, HttpSession session) {
        String sessionId = session.getId();
        model.addAttribute("orderForm", new OrderForm());
        model.addAttribute("cartItems", orderService.getCartItems(sessionId));
        model.addAttribute("totalPrice", orderService.calculateTotal(sessionId));
        return "user/order_form";
    }

    // 注文内容確認画面への遷移
    @PostMapping("/confirm")
    public String confirmOrder(
            @Valid @ModelAttribute("orderForm") OrderForm form,
            BindingResult bindingResult,
            HttpSession session,
            Model model) {

        String sessionId = session.getId();

        if (bindingResult.hasErrors()) {
            model.addAttribute("cartItems", orderService.getCartItems(sessionId));
            model.addAttribute("totalPrice", orderService.calculateTotal(sessionId));
            return "user/order_form";
        }

        model.addAttribute("orderForm", form);
        model.addAttribute("cartItems", orderService.getCartItems(sessionId));
        model.addAttribute("totalPrice", orderService.calculateTotal(sessionId));
        return "user/order_confirm";
    }

    // 注文完了処理
    @PostMapping("/complete")
    public String completeOrder(@ModelAttribute("orderForm") OrderForm form,
                                 HttpSession session,
                                 Model model) {
        String sessionId = session.getId();
        var order = orderService.processOrder(form, sessionId);
        model.addAttribute("order", order);
        return "user/order_complete";
    }
}
