package cn.yangself.leigodcontrol.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@ResponseBody
public class leigodController {

    /**
     * 使用账号登陆获取token
     * @return
     */
    @PostMapping("/token")
    public String getToken(){
        log.info("获取token");
        return "获取token";
    }
}
