package cn.yangself.leigodcontrol.service.impl;

import cn.yangself.leigodcontrol.utils.DigestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sun.security.provider.MD5;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class LeigodServiceImplTest {
    @Autowired
    private DigestUtils digestUtils;

    @Test
    void login() {
        //a39d37b9d9fa0c350d8832092dd7a6d9
        //523145794
        String pwd = "523145794";
        String s = digestUtils.md5Hex(pwd);
        log.info(s);
    }

    @Test
    public void timeFormat() throws ParseException {
        String time = "2020-07-31 00:23:52";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(time);
        log.info("存入的时间" + date);
    }
}