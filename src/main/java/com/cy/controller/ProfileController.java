package com.cy.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.cy.dto.PaginationDTO;
import com.cy.enums.NotificationStatusEnum;
import com.cy.mapper.UserMapper;
import com.cy.model.Notification;
import com.cy.model.User;
import com.cy.service.NotificationService;
import com.cy.service.QuestionService;

@Controller
public class ProfileController {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private NotificationService notificationService;

	@GetMapping("/profile/{action}")
	public String profile(HttpServletRequest request, @PathVariable(name = "action") String action, Model model,
			@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "redirect:/";
		}
		if ("questions".equals(action)) {
			model.addAttribute("section", "questions");
			model.addAttribute("sectionName", "我的提问");
			PaginationDTO<?> paginationDTO = questionService.list(user.getId(), page, size);
			model.addAttribute("pagination", paginationDTO);
		} else if ("replies".equals(action)) {
			PaginationDTO<?> paginationDTO=notificationService.list(user.getId(),page,size);
			model.addAttribute("section", "replies");
			model.addAttribute("pagination", paginationDTO);
			model.addAttribute("sectionName", "最新回复");
		}

		return "profile";
	}
}
