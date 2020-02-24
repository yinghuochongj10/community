package com.cy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cy.dto.CommentDTO;
import com.cy.dto.QuestionDTO;
import com.cy.enums.CommentTypeEnum;
import com.cy.service.CommentService;
import com.cy.service.QuestionService;


@Controller
public class QuestionController {
	@Autowired
	private QuestionService questionService;
	@Autowired
	private CommentService commentService;
	@GetMapping("/question/{id}")
	public String question(@PathVariable(name="id")Long id,Model model) { 
		QuestionDTO questionDTO=questionService.getById(id);
		List<QuestionDTO> relatedQuestions=questionService.selectRelated(questionDTO);
		List<CommentDTO>comments=commentService.ListByTargetId(id,CommentTypeEnum.QUESTION);
		//累加阅读数
		questionService.incView(id);
		model.addAttribute("question",questionDTO);
		model.addAttribute("comments",comments);
		model.addAttribute("relatedQuestions",relatedQuestions);
		return "question";
	}
}
