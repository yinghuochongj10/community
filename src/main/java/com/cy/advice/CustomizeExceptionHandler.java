package com.cy.advice;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.cy.dto.ResultDTO;
import com.cy.exception.CustomizeErroCode;
import com.cy.exception.CustomizeException;

@ControllerAdvice
public class CustomizeExceptionHandler {

	@ExceptionHandler(Exception.class)
	ModelAndView handle(Throwable e, Model model, HttpServletRequest request,HttpServletResponse response) {
		String contentType = request.getContentType();
		if ("application/json".equals(contentType)) {
			ResultDTO resultDTO;
			// 返回Json
			if (e instanceof CustomizeException) {
				resultDTO= ResultDTO.errorOf((CustomizeException)e);
			} else {
				resultDTO= ResultDTO.errorOf(CustomizeErroCode.SYS_ERROR);
			}
			try {
				response.setContentType("application/json");
				response.setStatus(200);
				response.setCharacterEncoding("utf-8");
				PrintWriter writer=response.getWriter();
				writer.write(JSON.toJSONString(resultDTO));
				writer.close();
			} catch (IOException ioe) {
							}

			return null;
		} else {
			// 错误页面跳转
			if (e instanceof CustomizeException) {
				model.addAttribute("message", e.getMessage());
			} else {
				model.addAttribute("message",CustomizeErroCode.SYS_ERROR.getMessage());
			}

			return new ModelAndView("error");
		}

	}

}
