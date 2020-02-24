package com.cy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.dto.FileDTO;

@Controller
public class FileController {
	@RequestMapping("/file/upload")
	@ResponseBody
	public FileDTO upload() {
		FileDTO fileDTO=new FileDTO();
		fileDTO.setSuccess(1);
		fileDTO.setUrl("/img/gou.jpg");
		return fileDTO;
	}
}
