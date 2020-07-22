package cn.yangself.leigodcontrol.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    //用户ID
    private Long userId;
    //用户名
    private String username;
    //密码
    private String password;
    //昵称
    private String nickname;
    //token
    private String token;
    //token过期时间
    private Date expiryTime;

}
