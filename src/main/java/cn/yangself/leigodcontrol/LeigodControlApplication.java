package cn.yangself.leigodcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan({"com.yangself.*.mapper"})
@EnableScheduling
public class LeigodControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeigodControlApplication.class, args);
    }

}
