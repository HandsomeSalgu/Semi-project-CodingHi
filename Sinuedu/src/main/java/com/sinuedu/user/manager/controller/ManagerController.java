package com.sinuedu.user.manager.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.sinuedu.user.manager.model.service.ManagerService;
import com.sinuedu.user.member.model.vo.Member;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
@RequestMapping("/manager")
public class ManagerController {
    
    private final ManagerService aService;
    
    @GetMapping("userList")
    public String userList(Model model) {
        List<Member> userList = aService.userList();
        System.out.println("User list size: " + userList.size());
        model.addAttribute("list", userList);
        return "/views/manager/userList"; 
    }
}
