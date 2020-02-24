package com.cy.service;

import java.util.List;

import com.cy.dto.PaginationDTO;
import com.cy.dto.QuestionDTO;
import com.cy.model.Question;
import com.cy.model.User;

public interface QuestionService {
	 //提交问题

	 
	 //处理中间层qurstionDTO
	 PaginationDTO list(Integer page, Integer size);



	void cerateOrUpdate(Question question);


	QuestionDTO getById(Long id);

	void incView(Long id);

	PaginationDTO list(Long userId, Integer page, Integer size);



	List<QuestionDTO> selectRelated(QuestionDTO questionDTO);
}
