package com.cy.service;

import java.util.List;

import com.cy.model.User;

public interface UserService {
	 //用户登录
	User login(User user);
	 //用户注册
	 void regist(User user);
	
}
