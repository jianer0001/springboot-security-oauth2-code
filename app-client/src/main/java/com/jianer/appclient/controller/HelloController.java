package com.jianer.appclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author 杨兴健
 * @Date 2020/4/21 10:29
 */
@Controller
public class HelloController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/index")
    public String hello(String code, Model model) {
        if (code != null) {
            LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("code",code);
            map.add("client_id","jianer");
            map.add("client_secret","jianer");
            map.add("redirect_uri","http://localhost:8082/index");
            map.add("grant_type","authorization_code");
            Map<String,String> resp = restTemplate.postForObject("http://localhost:8080/oauth/token", map, Map.class);
            String access_token = resp.get("access_token");
            System.out.println(access_token);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","Bearer " + access_token);
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> entity = restTemplate.exchange("http://localhost:8081/admin/hello", HttpMethod.GET, httpEntity, String.class);
            model.addAttribute("msg",entity.getBody());
        }
        return "index";
    }
}
