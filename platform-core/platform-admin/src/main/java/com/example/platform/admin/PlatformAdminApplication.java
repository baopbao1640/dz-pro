package com.example.platform.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({
  "com.platform.core.system.dept.mapper",
  "com.platform.core.system.menu.mapper",
  "com.platform.core.system.post.mapper",
  "com.platform.core.system.role.mapper",
  "com.platform.core.system.user.mapper"
})
@SpringBootApplication(scanBasePackages = {"com.example.platform.admin", "com.platform.core"})
public class PlatformAdminApplication {

  public static void main(String[] args) {
    SpringApplication.run(PlatformAdminApplication.class, args);
  }
}
