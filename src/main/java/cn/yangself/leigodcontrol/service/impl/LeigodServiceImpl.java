package cn.yangself.leigodcontrol.service.impl;

import cn.yangself.leigodcontrol.entity.User;
import cn.yangself.leigodcontrol.mapper.LeigodMapper;
import cn.yangself.leigodcontrol.service.ILeigodService;
import cn.yangself.leigodcontrol.utils.DigestUtils;
import cn.yangself.leigodcontrol.utils.NetRequest;
import cn.yangself.leigodcontrol.utils.UniqueIdGenerate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class LeigodServiceImpl implements ILeigodService {

    @Autowired
    private DigestUtils digestUtils;

    @Autowired
    private NetRequest netRequest;

    @Autowired
    private LeigodMapper leigodMapper;

    @Autowired
    private UniqueIdGenerate uniqueIdGenerate;

    @Value("${leigod.login}")
    private String loginApi;

    @Value("${leigod.pause}")
    private String pauseApi;

    @Value("${leigod.info}")
    private String infoApi;


    /**
     * 使用用户名和密码去获取token，并写入到数据库中
     *
     * @param username
     * @param password
     * @return 网站token
     */
    @Override
    public Map<String,Object> login(String username, String password) {
        Map<String,Object> resultMap = new HashMap<>();
        try {
            //将密码进行md5 加密
            String pwdMd5 = digestUtils.md5Hex(password);
            //使用账户和密码去雷神取出token
            Map qMap = new HashMap();
            qMap.put("lang", "zh_CN");
            qMap.put("password", pwdMd5);
            qMap.put("region_code", 1);
            qMap.put("src_channel", "guanwang");
            qMap.put("user_type", "0");
            qMap.put("username", username);
            Map resMap = netRequest.sendPost(loginApi, qMap);
            log.info("token =====> " + resMap);
            if (resMap != null) {
                if ("0".equals(String.valueOf(resMap.get("code")).trim())) {//如果官网状态码为0
                    //获取token成功
                    //将用户名，密码，以及用户信息存入数据库
                    Map<String, Object> data = (Map<String, Object>) resMap.get("data");
                    Map<String, String> loginInfo = (Map<String, String>) data.get("login_info");
                    //登陆token
                    String token = loginInfo.get("account_token");
                    //token有效期
                    String expiry = loginInfo.get("expiry_time");
                    Date expiryTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expiry);
                    Map<String, String> userInfo = (Map<String, String>) data.get("user_info");
                    //昵称
                    String nickname = userInfo.get("nickname");

                    //检查用户名是否在库中存在
                    User user = leigodMapper.selectUserByUsername(username);
                    //如果存在 更新token和有效期
                    if (user != null) {
                        user.setToken(token);
                        user.setExpiryTime(expiryTime);
                        int i = leigodMapper.updateUser(user);
                        if (i > 0) {
                            log.info("更新用户token成功！");
                        } else {
                            log.info("更新用户token失败！");
                        }
                    } else {
                        //如果不存在则新建用户
                        log.info("用户不存在，进行用户创建操作！");
                        User newUser = new User().builder()
                                .userId(uniqueIdGenerate.nextId())
                                .username(username)
                                .password(password)
                                .nickname(nickname)
                                .token(token)
                                .expiryTime(expiryTime)
                                .build();
                        int i = leigodMapper.addUser(newUser);
                        if (i > 0) {
                            log.info("创建用户成功！");
                        } else {
                            log.info("创建用户失败！");
                        }
                    }
                    resultMap.put("code", 200);
                    resultMap.put("msg", "获取token成功！");
                    resultMap.put("result", resMap);
                } else {
                    resultMap.put("code", 200);
                    resultMap.put("errorCode", 202);
                    resultMap.put("msg", "官网API获取状态异常！");
                    resultMap.put("result", resMap);
                }
            } else {
                resultMap.put("code", 200);
                resultMap.put("errorCode", 201);
                resultMap.put("msg", "官网API未获取到数据！");
            }
        }catch (Exception e){
            resultMap.put("code", 500);
            resultMap.put("errorCode", 203);
            resultMap.put("msg", "服务器发生错误！");
            resultMap.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    /**
     * 获取所有的用户信息
     *
     * @return
     */
    @Override
    public Map<String, Object> userList() {
        Map<String,Object> resultMap = new HashMap<>();
        List<Map<String,Object>> usersList = new ArrayList<>();
        //获取所有的用户信息
        //获取所有库中的user
        try {
            List<User> users = leigodMapper.selectAllUsers();
            if (users != null) {
                //使用user里面的token进行信息的查询
                for (User user : users) {//遍历用户
                    if (user.getToken() != null) {
                        try {
                            //进行获取个人信息请求
                            Map<String, Object> qMap = new HashMap<>();
                            qMap.put("account_token", user.getToken());
                            qMap.put("lang", "zh_CN");
                            //发出请求
                            Map resMap = netRequest.sendPost(infoApi, qMap);
                            usersList.add(resMap);
                        } catch (Exception e) {
                            log.info("获取个人信息异常！");
                            e.printStackTrace();
                        }
                    }
                }
            }

            resultMap.put("code",200);
            resultMap.put("msg","请求成功！");
            resultMap.put("result",usersList);
        }catch (Exception e){
            resultMap.put("code",500);
            resultMap.put("msg","服务器发生错误！");
            resultMap.put("errorMsg",e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 定时器进行暂停
     */
    @Scheduled(cron="0 5/20 3 * * ?")
    private void pauseAlert(){
        log.info("============ 检查所有账号token ============");
        tokenAlert();
        log.info("============ 开始暂停全部账号 ============");
        //获取所有库中的user
        List<User> users = leigodMapper.selectAllUsers();
        for (User user : users) {//遍历用户
            if (user.getToken() != null){
                try {
                //进行暂停
                Map<String,Object> qMap = new HashMap<>();
                qMap.put("account_token", user.getToken());
                qMap.put("lang", "zh_CN");
                //发出暂停请求
                Map map = netRequest.sendPost(pauseApi, qMap);
                }catch (Exception e){
                    log.info("暂停异常！");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 每个小时检查数据库中的token有没有失效
     */
    @Scheduled(cron="0 0 * * * ?")
    private void tokenAlert(){
        //获取所有库中的user
        List<User> users = leigodMapper.selectAllUsers();
        //获取用户信息，如果能获取说明token可以用，不能，重新获取
        for (User user : users) {//遍历用户
            if (user.getToken() != null) {//不等于null，请求详情
                Map<String,Object> qMap = new HashMap<>();
                qMap.put("account_token", user.getToken());
                qMap.put("lang", "zh_CN");
                //发出查询信息请求
                Map map = netRequest.sendPost(infoApi, qMap);
                if(map.get("code") != null && map.get("code").toString().equals("400006")){
                    //说明账号未登陆，获取token
                    login(user.getUsername(),user.getPassword());
                }
            }else{//等于空，直接获取token
                login(user.getUsername(),user.getPassword());
            }
        }

    }


}
