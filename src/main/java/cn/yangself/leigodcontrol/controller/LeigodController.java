package cn.yangself.leigodcontrol.controller;

import cn.yangself.leigodcontrol.service.ILeigodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@Slf4j
@ResponseBody
@RequestMapping("/leigod")
public class LeigodController {

    @Autowired
    private ILeigodService leigodService;

    /**
     * 使用账号登陆保存到系统中
     * @param sendMap 账号密码
     * @return
     */
    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody Map<String,String> sendMap){
        String username = sendMap.get("username");
        String password = sendMap.get("password");

        Map<String, Object> resultMap = leigodService.login(username, password);
        return resultMap;
    }

    /**
     * 批量账号状态获取
     * 是否暂停，账号剩余时间，上次暂停时间等
     * @return
     */
    @PostMapping("/userList")
    public Map<String,Object> userList(){
        return leigodService.userList();
    }




}
