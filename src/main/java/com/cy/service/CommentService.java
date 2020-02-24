package com.cy.service;

import java.util.List;

import com.cy.dto.CommentDTO;
import com.cy.enums.CommentTypeEnum;
import com.cy.model.Comment;
import com.cy.model.User;

public interface CommentService {

	void insert(Comment comment, User commentator);

	List<CommentDTO> ListByTargetId(Long id,CommentTypeEnum type);

}
