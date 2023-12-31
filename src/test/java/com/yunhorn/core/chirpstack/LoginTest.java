package com.yunhorn.core.chirpstack;

import com.google.common.collect.Maps;
import com.yunhorn.core.chirpstack.util.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author ljm
 * @date 2022/9/8 17:36
 */
@Slf4j
public class LoginTest {
    @Test
    public void loginTest(){
        String domain = System.getProperty("chirpstack.test.domain","");
        String account = System.getProperty("chirpstack.test.account","");
        String password = System.getProperty("chirpstack.test.password","");
        if (StringUtils.isBlank(domain) || StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return;
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        Map<String,String> loginMap = Maps.newHashMap();
        //最新的loraserver版本的API接口 username字段替换成了email
        loginMap.put("username", account);
        loginMap.put("email", account);
        loginMap.put("password",password);
        HttpEntity<?> entity = new HttpEntity<>(loginMap, httpHeaders);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(domain+"/api/internal/login", HttpMethod.POST,entity,Map.class);
        Map<String,Object> respMap = restTemplate.exchange(domain+"/api/internal/login", HttpMethod.POST,entity,Map.class).getBody();
        log.info("CheckExpireTokenTest.loginTest|{}|{}",respMap, JSONUtils.beanToJson(responseEntity));
    }
}
