package com.example.mgmgapp.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mgmgapp.entity.Admins;

public interface AdminRepository extends JpaRepository<Admins, Integer> {
    Optional<Admins> findByUserName(String user_name);
}
