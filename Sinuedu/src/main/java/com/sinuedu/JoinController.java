package com.sinuedu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class JoinController {
	
	@GetMapping("/view/join1")
	public String join() { 
		return "views/join/join1";
	}
	
	@GetMapping("/view/category")
	public String categoryjoin() {
		return "views/classes/postlist";
	}

}
