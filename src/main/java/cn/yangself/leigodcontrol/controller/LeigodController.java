package cn.yangself.leigodcontrol.controller;

import cn.yangself.leigodcontrol.service.ILeigodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@Slf4j
@ResponseBody
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

    //批量账号状态获取


}
