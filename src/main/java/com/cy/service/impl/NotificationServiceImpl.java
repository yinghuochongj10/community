package com.cy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.dto.NotificationDTO;
import com.cy.dto.PaginationDTO;
import com.cy.dto.QuestionDTO;
import com.cy.enums.NotificationStatusEnum;
import com.cy.enums.NotificationTypeEnum;
import com.cy.exception.CustomizeErroCode;
import com.cy.exception.CustomizeException;
import com.cy.mapper.NotificationMapper;
import com.cy.mapper.UserMapper;
import com.cy.model.Notification;
import com.cy.model.NotificationExample;
import com.cy.model.Question;
import com.cy.model.QuestionExample;
import com.cy.model.User;
import com.cy.model.UserExample;
import com.cy.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
	@Autowired
	private NotificationMapper notificationMapper;
	@Autowired
	private UserMapper userMapper;
	@Override
	public PaginationDTO<NotificationDTO> list(Long userId, Integer page, Integer size) {

		PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

		Integer totalPage;
		NotificationExample notificationExample = new NotificationExample();
		notificationExample.createCriteria().andReceiverEqualTo(userId);
		Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
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

		// size*(page-1)
		Integer offset = size * (page - 1);
		NotificationExample example = new NotificationExample();
		example.createCriteria().andReceiverEqualTo(userId);
		example.setOrderByClause("gmt_create desc");
		List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example,
				new RowBounds(offset, size));
		if (notifications.size() == 0) {
			return paginationDTO;
		}
		List<NotificationDTO> notificationDTOs = new ArrayList<>();
		for(Notification notification:notifications) {
			NotificationDTO notificationDTO=new NotificationDTO();
			BeanUtils.copyProperties(notification,notificationDTO);
			notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
			notificationDTOs.add(notificationDTO);
		}


		paginationDTO.setData(notificationDTOs);

		return paginationDTO;
	}
	@Override
	public Long unreadCount(Long userId) {
		NotificationExample notificationExample = new NotificationExample();
		notificationExample.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
		return notificationMapper.countByExample(notificationExample);
	}
	@Override
	public NotificationDTO read(Long id, User user) {
			
		Notification notification = notificationMapper.selectByPrimaryKey(id);
		if(notification==null) {
			throw new CustomizeException(CustomizeErroCode.NOTIFICATION_NOT_FOUND);
		}
		if(!Objects.equals(notification.getReceiver(),user.getId())) {
			throw new CustomizeException(CustomizeErroCode.READ_NOTIFICATION_FALL);
		}
		notification.setStatus(NotificationStatusEnum.READ.getStatus());
		notificationMapper.updateByPrimaryKey(notification);
		
		NotificationDTO notificationDTO=new NotificationDTO();
		BeanUtils.copyProperties(notification,notificationDTO);
		notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
		return notificationDTO;
	}

}
