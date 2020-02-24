package com.cy.service;

import com.cy.dto.NotificationDTO;
import com.cy.dto.PaginationDTO;
import com.cy.model.User;

public interface NotificationService {

	PaginationDTO<?> list(Long id, Integer page, Integer size);

	Long unreadCount(Long id);

	NotificationDTO read(Long id, User user);

}
