package com.cy.dto;

import java.util.List;

import lombok.Data;

@Data
public class TagDTO {
	private String categoryName;
	private List<String> tags;
}
