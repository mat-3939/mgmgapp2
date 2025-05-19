package com.example.mgmgapp.service.admin;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.mgmgapp.entity.Admins;
import com.example.mgmgapp.repository.admin.AdminRepository;

@Service
public class ServiceImpl implements AdminLoginService {

    @Autowired
    private AdminRepository adminRepository;

    //ログイン時、Spring Security にこのインターフェースの loadUserByUsername(String username) メソッドを自動で呼び出してもらう。
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admins admin = adminRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));

        return new User(
                admin.getUserName(),
                admin.getPassword(),
                Collections.emptyList() // 空の権限リストを明示的に渡す。
        );
    }
}

