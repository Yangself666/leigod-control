package cn.yangself.leigodcontrol.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个类用来发送Post请求
 */
@Component
@Slf4j
public class NetRequest {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 发送post请求
     *
     * @param paramsMap
     * @param url
     * @return
     */
    public Map sendPost(String url, Map paramsMap) {
        HttpHeaders headers = new HttpHeaders();
        return sendPost(url, paramsMap, headers);
    }

    /**
     * 发送post请求
     *
     * @param paramsMap
     * @param url
     * @return
     */
    public Map sendPost(String url, Map paramsMap, HttpHeaders headers) {
        Map res = new HashMap();
        try {
            headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
            //用HttpEntity封装整个请求报文
            HttpEntity<String> httpEntity = new HttpEntity(paramsMap, headers);
            log.info("请求地址 =====> " + url);
            String backInfo = getRestTemplateBuilder().postForObject(url, httpEntity, String.class);
            res = JSON.parseObject(backInfo);
        } catch (Exception e) {
            res.put("code", 500);
            res.put("success", false);
            res.put("messgae", "参数有误");
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 发送post请求(自行设定请求类型)
     *
     * @param paramsMap
     * @param url
     * @return
     */
    public Map sendUrlencodedPost(String url, Map paramsMap) {
        Map res = new HashMap();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type","application/x-www-form-urlencoded");
            //用HttpEntity封装整个请求报文
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();

            paramsMap.forEach((key,value) -> {
                form.add(key.toString(), value);
            });
            HttpEntity<String> httpEntity = new HttpEntity(form, headers);
            log.info("请求地址 =====> " + url);
            String backInfo = getRestTemplateBuilder().postForObject(url, httpEntity, String.class);
            res = JSON.parseObject(backInfo);
        } catch (Exception e) {
            res.put("code", 500);
            res.put("success", false);
            res.put("messgae", "参数有误");
            e.printStackTrace();
        }
        return res;
    }

    public RestTemplate getRestTemplateBuilder() {
        List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
        httpMessageConverters.stream().forEach(httpMessageConverter -> {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) httpMessageConverter;
                messageConverter.setDefaultCharset(Charset.forName("UTF-8"));
            }
        });
        return restTemplate;
    }

}
