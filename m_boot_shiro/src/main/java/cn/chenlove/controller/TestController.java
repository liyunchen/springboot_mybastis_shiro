package cn.chenlove.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.chenlove.model.User;

@Controller
public class TestController {
          
	 @RequestMapping(value="/test")
	 public ModelAndView test1() {
		 ModelAndView  model  = new ModelAndView();
		 model.addObject("name", "张三");
		 model.addObject("age", 13);
		 User user0 = new User();
		 user0.setId(0);
		 user0.setUsername("张三");
		 model.addObject("user", user0);
		 List<User> list = new ArrayList<User>();
		 for(int i =1;i<=5;i++) {
			 User user = new User();
			 user.setId(i);
			 user.setUsername("张三"+i);
			 list.add(user);
	     }
		 model.addObject("prods", list);
		 model.setViewName("test/test1");
		 return model;
	 }
}
