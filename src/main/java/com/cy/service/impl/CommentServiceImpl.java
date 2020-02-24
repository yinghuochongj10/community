package com.cy.service.impl;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cy.dto.CommentDTO;
import com.cy.enums.CommentTypeEnum;
import com.cy.enums.NotificationStatusEnum;
import com.cy.enums.NotificationTypeEnum;
import com.cy.exception.CustomizeErroCode;
import com.cy.exception.CustomizeException;
import com.cy.mapper.CommentExtMapper;
import com.cy.mapper.CommentMapper;
import com.cy.mapper.NotificationMapper;
import com.cy.mapper.QuestionMapper;
import com.cy.mapper.UserMapper;
import com.cy.model.Comment;
import com.cy.model.CommentExample;
import com.cy.model.Notification;
import com.cy.model.Question;
import com.cy.model.User;
import com.cy.model.UserExample;
import com.cy.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private QuestionMapper questionMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CommentExtMapper commentExtMapper;
	@Autowired
	private NotificationMapper notificationMapper;

	@Transactional
	@Override
	public void insert(Comment comment, User commentator) {
		comment.setCommentCount(0);
		if (comment.getParentId() == null && comment.getParentId() == 0) {
			throw new CustomizeException(CustomizeErroCode.TAGET_PARAM_NOT_FOUND);
		}
		if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
			throw new CustomizeException(CustomizeErroCode.TYPE_PARAM_WRONG);
		}
		if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
			// 回复评论
			Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
			if (dbComment == null) {
				throw new CustomizeException(CustomizeErroCode.COMMENT_NOT_FOUND);
			}
			// 回复问题
			Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
			if (question == null) {
				throw new CustomizeException(CustomizeErroCode.QUESTION_NOT_FOUND);

			}

			commentMapper.insert(comment);
			// 增加评论数
			Comment parentComment = new Comment();
			parentComment.setId(comment.getParentId());
			parentComment.setCommentCount(1);
			commentExtMapper.incCommentCount(parentComment);
			// 创建通知
			createNotify(comment, dbComment.getCommentator(), commentator.getUsername(), question.getTitle(),
					NotificationTypeEnum.REPLY_COMMENT,question.getId());
		} else {
			// 回复问题
			Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
			if (question == null) {
				throw new CustomizeException(CustomizeErroCode.QUESTION_NOT_FOUND);

			}

			commentMapper.insert(comment);
			question.setCommentCount(1);
			questionMapper.incCommentCount(question);

			// 创建通知
			createNotify(comment, question.getCreator(), commentator.getUsername(), question.getTitle(),
					NotificationTypeEnum.REPLY_QUESTION,question.getId());

		}
	}

	private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle,
			NotificationTypeEnum notificationType,Long outerId) {
		Notification notification = new Notification();
		notification.setGmtCreate(System.currentTimeMillis());
		notification.setType(notificationType.getType());
		notification.setOuterid(outerId);
		notification.setNotifier(comment.getCommentator());
		notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
		notification.setReceiver(receiver);
		notification.setNotifierName(notifierName);
		notification.setOuterTitle(outerTitle);
		notificationMapper.insert(notification);
	}

	@Override
	public List<CommentDTO> ListByTargetId(Long id, CommentTypeEnum type) {
		CommentExample commentExample = new CommentExample();
		commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
		commentExample.setOrderByClause("gmt_create desc");
		List<Comment> comments = commentMapper.selectByExample(commentExample);
		if (comments.size() == 0) {
			return new ArrayList<>();
		}
		// 获取去重的评论人
		Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
		List<Long> userIds = new ArrayList();
		userIds.addAll(commentators);
		// 获取评论人并转换为Map
		UserExample userExample = new UserExample();
		userExample.createCriteria().andIdIn(userIds);
		List<User> users = userMapper.selectByExample(userExample);
		Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
		// 转换comment为commentDTO
		List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
			CommentDTO commentDTO = new CommentDTO();
			BeanUtils.copyProperties(comment, commentDTO);
			commentDTO.setUser(userMap.get(comment.getCommentator()));
			return commentDTO;
		}).collect(Collectors.toList());
		return commentDTOs;
	}

}
