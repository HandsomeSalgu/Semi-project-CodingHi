package com.sinuedu.user.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class RecaptchaController {

    @GetMapping("/verify-recaptcha")
    public ResponseEntity<String> verifyRecaptcha(@RequestParam("token") String recaptchaToken) {
        String secretKey = "6Ld0rsEqAAAAALmVb9_5kZo1XDunzoxYuj3ulEIx"; // Google 비밀 키
        String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> body = new HashMap<>();
        body.put("secret", secretKey);
        body.put("response", recaptchaToken);

        // Google API에 검증 요청
        ResponseEntity<Map> apiResponse = restTemplate.postForEntity(verifyUrl, body, Map.class);

        // 응답 확인
        Map<String, Object> responseBody = apiResponse.getBody();
        if (responseBody != null && Boolean.TRUE.equals(responseBody.get("success"))) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail");
        }
    }
}
