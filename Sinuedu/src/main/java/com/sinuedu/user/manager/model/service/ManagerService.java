package com.sinuedu.user.manager.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sinuedu.user.manager.model.mapper.ManagerMapper;
import com.sinuedu.user.member.model.vo.Member;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ManagerService {
    
    private final ManagerMapper mapper;
    
    public List<Member> userList() {
        try {
            List<Member> list = mapper.userList();
            System.out.println("조회된 회원 수: " + (list != null ? list.size() : "null"));
            return list;
        } catch (Exception e) {
            System.out.println("회원 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}