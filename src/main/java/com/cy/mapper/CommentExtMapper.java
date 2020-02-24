package com.cy.mapper;

import com.cy.model.Comment;
import com.cy.model.CommentExample;
import com.cy.model.Question;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface CommentExtMapper{
	int  incCommentCount(Comment comment);
}