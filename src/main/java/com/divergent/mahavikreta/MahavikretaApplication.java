package com.divergent.mahavikreta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MahavikretaApplication {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public static void main(String[] args){
		SpringApplication.run(MahavikretaApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void jdbcQuery() {
//		try { 
//			jdbcTemplate.update("INSERT INTO `role` (`id`, `display_name`, `name`) VALUES" +
//					"('1', 'acquire_points', 'ROLE_SUPER_ADMIN')," +
//					"('2', 'add_an_image', 'ROLE_ADMIN')," +
//					"('3', 'bottles', 'ROLE_USER');");
//		} catch (Exception e) {
//		}
//		try {
//			jdbcTemplate.update("INSERT INTO `users` (`id`,`account_non_expired`,`account_non_locked`," +
//					"`credentials_non_expired`,`email`,`enabled`,`first_name`,`gstin`,`last_name`,`password`," +
//					"`shop_name`,`username`, `rzpay_cust_id`) VALUES ('1',true,true,true,'admin@admin.com',true,'Super'," +
//					"'XYZ123ABC','Admin','$2a$10$U0fGKikUZ.kWwbhqsSO0iukpwX3fh1PCOOhiv7XfrAu2N.T2/K9bO','Admin','admin','12345');");
//		} catch (Exception e) {
//		}
		try {
			jdbcTemplate.update("INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES ('1', 'ROLE_SUPER_ADMIN');");
		} catch (Exception e) {
		}

	}

}
