package com.yunhorn.core.chirpstack.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

/**
 * @author ljm
 * @date 2021/12/14 15:45
 */
public class RestTemplateUtil {
    private final RestTemplate restTemplate = new RestTemplate();
    private static RestTemplateUtil restTemplateUtil = null;
    private RestTemplateUtil(){

    }
    public static RestTemplateUtil getInstance(){
        if (restTemplateUtil==null){
            restTemplateUtil = new RestTemplateUtil();
        }
        return restTemplateUtil;
    }

    private <T> T request(String url,Object body, Map<String, String> headers, Class<T> responseType,HttpMethod httpMethod){
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers!=null){
            headers.forEach(httpHeaders::add);
        }
        HttpEntity<?> entity = new HttpEntity<>(body, httpHeaders);
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            /*
            Some exceptions cannot be caught, such as request timeout.
            The exceptions that can be caught, such as token expiration, password error (the ResponseEntity object can be returned below the former, and the latter can throw an exception directly)
             */
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response){
            }
        });
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod,entity,responseType);
        return responseEntity.getBody();
    }

    public <T> T post(String url,Object body, Map<String, String> headers, Class<T> responseType){
        return request(url,body,headers,responseType,HttpMethod.POST);
    }

    public <T> T get(String url,Object body, Map<String, String> headers, Class<T> responseType){
        String json = JSONUtils.beanToJson(body);
        Map<String, Object> jsonMap = JSONUtils.jsonToMap(json);
        if (jsonMap!=null && !jsonMap.isEmpty()){
            //restTemplate的GET方法不能通过HttpEntity将请求参数传进去，会获取到空数据，必须将请求参数拼URL
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            jsonMap.forEach(builder::queryParam);
            url = builder.build().encode().toString();
        }
        return request(url,body,headers,responseType,HttpMethod.GET);
    }

    public <T> T put(String url,Object body, Map<String, String> headers, Class<T> responseType){
        return request(url,body,headers,responseType,HttpMethod.PUT);
    }

    public <T> T delete(String url,Object body, Map<String, String> headers, Class<T> responseType){
        return request(url,body,headers,responseType,HttpMethod.DELETE);
    }

}
