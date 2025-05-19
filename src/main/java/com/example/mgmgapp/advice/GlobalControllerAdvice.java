package com.example.mgmgapp.advice;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.mgmgapp.service.user.CartService;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CartService cartService;

    @ModelAttribute("cartTotalQuantity")
    public int cartTotalQuantity(HttpSession session) {
        String sessionId = session.getId();
        return cartService.getTotalQuantity(sessionId);
    }
}

