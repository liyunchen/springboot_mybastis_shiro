package cn.chenlove;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "cn.chenlove.mapper")
public class MBootShiroApplication{

	
	public static void main(String[] args) {
		SpringApplication.run(MBootShiroApplication.class, args);
	}
}
