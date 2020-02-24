package com.cy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.mapper.UserMapper;
import com.cy.model.User;
import com.cy.model.UserExample;
import com.cy.service.UserService;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Override
	public User login(User user) {
		System.out.println();
		UserExample userExample=new UserExample();
		userExample.createCriteria().andUsernameEqualTo(user.getUsername()).andPasswordEqualTo(user.getPassword());
		List<User> users=userMapper.selectByExample(userExample);
		System.out.println(users);
		if(users.size()!=0) {
			return users.get(0);
		}
		
		return null;
	}

	@Override
	public void regist(User user) {
		user.setUserimg("s");
		userMapper.insert(user);
	}
}
