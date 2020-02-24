package com.cy.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cy.dto.PaginationDTO;
import com.cy.dto.QuestionDTO;
import com.cy.mapper.UserMapper;
import com.cy.model.Question;
import com.cy.model.User;
import com.cy.model.UserExample;
import com.cy.service.QuestionService;


@Controller
public class IndexController {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private QuestionService questionService;

	@GetMapping("/")
	public String index(HttpServletRequest request, Model model,
			@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) {
		PaginationDTO pagination= questionService.list(page,size);
		model.addAttribute("pagination", pagination);
		return "index";
	}
	@GetMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		request.getSession().removeAttribute("user");
		Cookie cookie =new Cookie("username" ,null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		return "redirect:/";
	}
	
	
}
