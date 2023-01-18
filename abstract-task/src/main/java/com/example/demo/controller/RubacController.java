package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Param;
import com.example.demo.service.RubacService;

@RestController
public class RubacController {

	@Autowired
	private RubacService rubacService;
	
	@PostMapping("{*path}")
	public Boolean test(@RequestBody Param param, @PathVariable(value = "path") String path) {
		return rubacService.checkRule(param);
	}
}
