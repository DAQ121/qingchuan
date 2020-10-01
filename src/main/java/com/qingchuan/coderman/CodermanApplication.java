package com.qingchuan.coderman;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.qingchuan.coderman.*.dao")
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling   // 1.开启定时任务
@ServletComponentScan
@SuppressWarnings("all")
public class CodermanApplication {
	public static void main(String[] args)throws Exception{
		SpringApplication.run(CodermanApplication.class, args);
	}
}
