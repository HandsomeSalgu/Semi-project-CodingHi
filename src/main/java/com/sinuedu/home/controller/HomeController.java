package com.sinuedu.home.controller;


import com.sinuedu.home.service.HomeService;
import com.sinuedu.user.member.model.vo.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class HomeController {

    private final HomeService homeService;



    @GetMapping("/home")
    public ModelAndView homePage(ModelAndView mav, HttpSession session) {

        Member loginUser = (Member) session.getAttribute("loginUser");


        if (loginUser == null) {
            mav.setViewName("redirect:/");
            return mav;
        }
        Integer userNo = (Integer) loginUser.getUserNo();
        Map<String, Object> homeData = homeService.getHomeData(userNo);

        mav.addAllObjects(homeData);


        mav.setViewName("views/home");

        return mav;
    }
}
