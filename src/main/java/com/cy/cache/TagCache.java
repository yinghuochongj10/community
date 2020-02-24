package com.cy.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.cy.dto.TagDTO;

public class TagCache {
	public static List<TagDTO> get(){
		List<TagDTO> tagDTOs = new ArrayList<>();
		
		TagDTO program=new TagDTO();
		program.setCategoryName("开发语言");
		program.setTags(Arrays.asList("js","php","css","html","java","node","python"));
		tagDTOs.add(program);
		
		TagDTO framework=new TagDTO();
		framework.setCategoryName("平台框架");
		framework.setTags(Arrays.asList("js","php","css","html","java"));
		tagDTOs.add(framework);
		return tagDTOs;
	}
	public static String filterInvalid(String tags) {
		String[] split = StringUtils.split(tags,",");
		List<TagDTO>tagDTOs=get();
		List<String> tagList = tagDTOs.stream().flatMap(tag->tag.getTags().stream()).collect(Collectors.toList());
		String invalid=Arrays.stream(split).filter(t->!tagList.contains(t)).collect(Collectors.joining(","));
		return invalid;
	}
}
