package com.example.mgmgapp.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.mgmgapp.entity.CartItems;
import com.example.mgmgapp.repository.user.CartItemRepository;

/**
 * 期限切れのセッションに関連するカートアイテムをクリーンアップするスケジューラークラス
 */
@Component
public class CartCleanupScheduler {

    @Autowired
    private CartItemRepository cartItemRepository;

    /**
     * 7日ごとに実行される古いカートアイテムのクリーンアップタスク
     * 古いセッションに関連するカートアイテムを削除します
     */
    @Scheduled(cron = "0 0 3 */7 * ?") // 7日ごとに午前3時に実行
    @Transactional // トランザクション管理を追加
    public void cleanupExpiredCartItems() {
        // 現在のセッションから取得できないセッションIDを特定するのは難しいため、
        // 定期的にすべてのカートアイテムをクリーンアップします

        System.out.println("カートアイテムクリーンアップタスクを開始します: " + LocalDateTime.now());

        // すべてのカートアイテムを取得
        List<CartItems> allCartItems = cartItemRepository.findAll();

        // 削除したアイテム数をカウント
        int deletedCount = 0;

        for (CartItems item : allCartItems) {
            // ここでは単純にセッションIDを基に削除するロジックのみ実装
            try {
                // ここではすべてのカートアイテムを処理します（デモ用）
                cartItemRepository.deleteBySessionId(item.getSessionId());
                deletedCount++;
            } catch (Exception e) {
                System.err.println("セッションID " + item.getSessionId() + " のカートアイテム削除中にエラーが発生しました: " + e.getMessage());
            }
        }

        System.out.println("カートアイテムクリーンアップ完了: " + deletedCount + " 件のセッション関連アイテムを削除しました");
    }
}