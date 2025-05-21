package com.example.mgmgapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
/*クリーンアップ処理テスト用*/
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.mgmgapp.scheduler.CartCleanupScheduler;
/*クリーンアップ処理テスト用ここまで*/

@SpringBootApplication
@EnableScheduling
public class MgmgappApplication {

/*クリーンアップ処理テスト用*/
	@Autowired
	private CartCleanupScheduler cartCleanupScheduler;
/*クリーンアップ処理テスト用ここまで*/

	public static void main(String[] args) {
		SpringApplication.run(MgmgappApplication.class, args);
	}

/*クリーンアップ処理テスト用*/
	@Scheduled(fixedRate = 60000) // 60秒（1分）ごとに実行
	public void scheduledTask() {
		// クリーンアップ処理のテスト呼び出し
		System.out.println("テスト用スケジューラーからカートクリーンアップ処理を呼び出します");
		cartCleanupScheduler.cleanupExpiredCartItems();
	}
/*クリーンアップ処理テスト用ここまで*/

}
