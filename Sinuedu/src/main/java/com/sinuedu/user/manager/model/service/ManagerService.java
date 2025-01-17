package com.sinuedu.user.manager.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sinuedu.user.manager.model.mapper.ManagerMapper;
import com.sinuedu.user.member.model.mapper.MemberMapper;
import com.sinuedu.user.member.model.vo.Member;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ManagerService {
    
    private final ManagerMapper mapper;

    public List<Member> userList() {
        List<Member> users = mapper.userList();
        System.out.println("Service - User count: " + users.size());
        return users;
    }
}
