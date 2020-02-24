package com.cy.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.dto.PaginationDTO;
import com.cy.dto.QuestionDTO;
import com.cy.exception.CustomizeErroCode;
import com.cy.exception.CustomizeException;

import com.cy.mapper.QuestionMapper;
import com.cy.mapper.UserMapper;
import com.cy.model.Question;
import com.cy.model.QuestionExample;
import com.cy.model.User;
import com.cy.model.UserExample;
import com.cy.service.QuestionService;


@Service
public class QuestionServiceImpl implements QuestionService {
	@Autowired
	private QuestionMapper questionMapper;
	@Autowired
	private UserMapper userMapper;



	@Override
	public PaginationDTO list(Integer page, Integer size) {
		PaginationDTO paginationDTO = new PaginationDTO();
		Integer totalPage;
		QuestionExample example = new QuestionExample();
		Integer totalCount = (int) questionMapper.countByExample(example);
		if (totalCount % size == 0) {
			totalPage = totalCount / size;
		} else {
			totalPage = totalCount / size + 1;
		}

		if (page < 1) {
			page = 1;
		}
		if (page > totalPage) {
			page = totalPage;
		}
		paginationDTO.setPage(totalPage, page);
		Integer offset = size * (page - 1);
		example.setOrderByClause("gmt_create desc");
		List<Question> questions=questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset,size));
		
		List<QuestionDTO> questionDTOList = new ArrayList<>();

		for (Question question : questions) {
			User user = userMapper.selectByPrimaryKey(question.getCreator());
			QuestionDTO questionDTO = new QuestionDTO();
			BeanUtils.copyProperties(question, questionDTO);
			questionDTO.setUser(user);
			questionDTOList.add(questionDTO);
		}
		paginationDTO.setData(questionDTOList);

		return paginationDTO;

	}

	@Override
	public PaginationDTO list(Long userId, Integer page, Integer size) {
		PaginationDTO paginationDTO = new PaginationDTO();
		QuestionExample example = new QuestionExample();
		Integer totalPage;
		QuestionExample questionExample=new QuestionExample();
		questionExample.createCriteria().andCreatorEqualTo(userId);
		Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
		if (totalCount % size == 0) {
			totalPage = totalCount / size;
		} else {
			totalPage = totalCount / size + 1;
		}

		if (page < 1) {
			page = 1;
		}
		if (page > totalPage) {
			page = totalPage;
		}
		paginationDTO.setPage(totalPage, page);
		Integer offset = size * (page - 1);
		example.setOrderByClause("gmt_create desc");
		
		example.createCriteria().andCreatorEqualTo(userId);
		
		List<Question> questions=questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset,size));
		List<QuestionDTO> questionDTOList = new ArrayList<>();

		for (Question question : questions) {
			User user = userMapper.selectByPrimaryKey(question.getCreator());
			QuestionDTO questionDTO = new QuestionDTO();
			BeanUtils.copyProperties(question, questionDTO);
			questionDTO.setUser(user);
			questionDTOList.add(questionDTO);
		}
		paginationDTO.setData(questionDTOList);

		return paginationDTO;

	}

	@Override
	public QuestionDTO getById(Long id) {
		Question question=questionMapper.selectByPrimaryKey(id);
		if(question==null) {
		throw new CustomizeException(CustomizeErroCode.QUESTION_NOT_FOUND);
		}
		QuestionDTO questionDTO = new QuestionDTO();
		BeanUtils.copyProperties(question, questionDTO);
		User user = userMapper.selectByPrimaryKey(question.getCreator());
		questionDTO.setUser(user);
		return questionDTO;
	}

	@Override
	public void cerateOrUpdate(Question question) {
		if(question.getId()==null) {
			//创建
			question.setGmtCreate(System.currentTimeMillis());
			question.setGmtModified(question.getGmtCreate());
			question.setViewCount(0);
			question.setLikeCount(0);
			question.setCommentCount(0);
			questionMapper.insert(question);
		}
		else {
			Question updateQuestion=new Question();
			updateQuestion.setGmtCreate(System.currentTimeMillis());
			updateQuestion.setTitle(question.getTitle());
			updateQuestion.setDescription(question.getDescription());
			updateQuestion.setTag(question.getTag());
			QuestionExample example=new QuestionExample();
			example.createCriteria().andIdEqualTo(question.getId());
			int updated=questionMapper.updateByExampleSelective(updateQuestion,example);
			if(updated!=1) {
				throw new CustomizeException(CustomizeErroCode.QUESTION_NOT_FOUND);
			}
		}
		
	}

	@Override
	public void incView(Long id) {

		Question question=new Question();
		question.setId(id);
		question.setViewCount(1);
		questionMapper.incView(question);
		
	}

	@Override
	public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
		if(StringUtils.isBlank(queryDTO.getTag())) {
			return new ArrayList<>();
		}
		String[] tags = StringUtils.split(queryDTO.getTag(),",");
		String regexTag = Arrays.stream(tags).collect(Collectors.joining("|"));
		Question question=new Question();
		question.setId(queryDTO.getId());
		question.setTag(regexTag);
		List<Question> questions = questionMapper.selectRelated(question);
		List<QuestionDTO> questionDTOs = questions.stream().map(q ->{
			
			QuestionDTO questionDTO=new QuestionDTO();
			BeanUtils.copyProperties(q, questionDTO);
			return questionDTO;}).collect(Collectors.toList());
		return questionDTOs;
	}






}
