package com.example.mgmgapp.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
import com.example.mgmgapp.entity.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface AdminOrderRepository extends JpaRepository<Orders, Integer> {
    
    /* 姓で検索 */
    List<Orders> findByLastNameOrderByOrderDateDesc(String lastName);
    
    /* 名で検索 */
    List<Orders> findByFirstNameOrderByOrderDateDesc(String firstName);
    
    /* 姓を含む検索 */
    List<Orders> findByLastNameContainingOrderByOrderDateDesc(String lastName);
    
    /* 名を含む検索 */
    List<Orders> findByFirstNameContainingOrderByOrderDateDesc(String firstName);

    /* メールアドレスで検索 */
    Orders findByEmailOrderByOrderDateDesc(String email);

    /* 電話番号で検索（複数結果を返す） */
    List<Orders> findByTelOrderByOrderDateDesc(String tel);

    /* 注文ステータスで検索 */
    List<Orders> findByStatus(Boolean status);
    
    /* 注文日時の範囲で検索 */
    List<Orders> findByOrderDateBetweenOrderByOrderDateDesc(LocalDateTime start, LocalDateTime end);
    
    /* 合計金額が指定値より大きい注文を検索 */
    List<Orders> findByTotalPriceGreaterThanOrderByTotalPriceDesc(BigDecimal amount);
    
    /* 注文日時でソートして検索 */
    List<Orders> findAllByOrderByOrderDateDesc();
    
    /* 特定の顧客の最新の注文を取得 */
    Orders findTopByFirstNameAndLastNameOrderByOrderDateDesc(String firstName, String lastName);
    
    /* 複合検索条件のメソッド */
    List<Orders> findByOrderDateBetweenAndTotalPriceGreaterThanOrderByOrderDateDesc(
        LocalDateTime start, LocalDateTime end, BigDecimal amount);
    
    /* メールアドレスを含む検索 */
    List<Orders> findByEmailContainingOrderByOrderDateDesc(String email);
    
    /* 電話番号を含む検索 */
    List<Orders> findByTelContainingOrderByOrderDateDesc(String tel);
    
    /* 複数条件のカスタムクエリ */
    @Query("SELECT o FROM Orders o WHERE " +
           "(:lastName IS NULL OR o.lastName LIKE %:lastName%) AND " +
           "(:firstName IS NULL OR o.firstName LIKE %:firstName%) AND " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:minAmount IS NULL OR o.totalPrice >= :minAmount) AND " +
           "(:startDate IS NULL OR o.orderDate >= :startDate) AND " +
           "(:endDate IS NULL OR o.orderDate <= :endDate) " +
           "ORDER BY o.orderDate DESC")
    List<Orders> searchOrders(
        @Param("lastName") String lastName,
        @Param("firstName") String firstName,
        @Param("status") Boolean status,
        @Param("minAmount") BigDecimal minAmount,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    /* 注文ステータスで検索（リスト） */
    List<Orders> findAllByStatus(Boolean status);

    long countByOrderDateBetween(LocalDateTime start, LocalDateTime end);
}
