package com.cy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.dto.CommentCreateDTO;
import com.cy.dto.CommentDTO;
import com.cy.dto.ResultDTO;
import com.cy.enums.CommentTypeEnum;
import com.cy.exception.CustomizeErroCode;
import com.cy.mapper.CommentMapper;
import com.cy.model.Comment;
import com.cy.model.User;
import com.cy.service.CommentService;

@Controller
public class CommentController {
	@Autowired
	private CommentService commentService;
	@ResponseBody
	@RequestMapping(value="/comment",method=RequestMethod.POST)
	public Object post(@RequestBody CommentCreateDTO commentCreateDTO,HttpServletRequest request) {
		User user=(User) request.getSession().getAttribute("user");
		if(user==null) {
			return ResultDTO.errorOf(CustomizeErroCode.NO_LOGIN);
		}
		if(commentCreateDTO==null|| commentCreateDTO.getContent()==null||commentCreateDTO.getContent()=="") {
			return ResultDTO.errorOf(CustomizeErroCode.COMMENT_IS_EMPTY);
		}
		Comment comment=new Comment();
		comment.setParentId(commentCreateDTO.getParentId());
		comment.setContent(commentCreateDTO.getContent());
		comment.setType(commentCreateDTO.getType());
		comment.setGmtCreate(System.currentTimeMillis());
		comment.setGmtModified(System.currentTimeMillis());
		comment.setCommentator(user.getId());
		comment.setLikeCount(0L);
		commentService.insert(comment,user);
		return ResultDTO.okof();
		
	}
	@ResponseBody
	@RequestMapping(value="/comment/{id}",method=RequestMethod.GET)
	public ResultDTO<List> comments(@PathVariable(name="id")Long id) {
		List<CommentDTO> commentDTOS= commentService.ListByTargetId(id, CommentTypeEnum.COMMENT);
		return ResultDTO.okof(commentDTOS);
	}
}
