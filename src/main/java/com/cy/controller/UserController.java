package com.cy.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.mapper.UserMapper;
import com.cy.model.User;
import com.cy.model.UserExample;
import com.cy.service.UserService;


@Controller
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserMapper userMapper;

	@GetMapping("/regist")
	public String regist() {
		return "regist";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/regist")
	@ResponseBody
	public String regist(User user) {
		try {
			UserExample userExample=new UserExample();
			userExample.createCriteria().andUsernameEqualTo(user.getUsername());
			List<User>exUser=userMapper.selectByExample(userExample);
			if (exUser.size()!=0) {
				return "存在";
			} else {
				userService.regist(user);
				return "成功";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/login")
	public String login(User user,HttpServletRequest request, HttpServletResponse response) {
		try {
			user = userService.login(user);
			Cookie cookie=new Cookie("username",user.getUsername());
			if (user!= null) {
				response.addCookie(cookie);
				System.out.println(user);
			}
			return "redirect:/";
		}
		catch(Exception e){
			
			e.printStackTrace();
		}
		return "login";

	}
}
