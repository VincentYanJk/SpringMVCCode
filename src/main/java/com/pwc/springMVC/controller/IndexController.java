package com.pwc.springMVC.controller;

import com.pwc.springMVC.annotation.Controller;
import com.pwc.springMVC.annotation.RequestMapping;

@Controller
@RequestMapping("/jason")
public class IndexController {
@RequestMapping("/index")
	public void index() {
		
		System.out.println("invoke index method start.");
		
	}
	
@RequestMapping("/search")
public void search() {
	
	System.out.println("invoke search method start.");
	
}

@RequestMapping("/add")
public void add() {
	
	System.out.println("invoke add method start.");
	
}

@RequestMapping("/delete")
public void delete() {
	
	System.out.println("invoke delete method start.");
	
}
}
