package cn.yangself.leigodcontrol.service;


import java.util.Map;

public interface ILeigodService {
    /**
     * 使用用户名和密码去获取token
     * @param username
     * @param password
     * @return 网站token
     */
    public Map<String,Object> login(String username,String password);

    /**
     * 获取所有的用户信息
     * @return
     */
    public Map<String,Object> userList();

}
