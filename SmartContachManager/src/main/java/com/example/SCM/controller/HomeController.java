package com.example.SCM.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SCM.DAO.UserRepo;
import com.example.SCM.helper.Message;
import com.example.SCM.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	UserRepo userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","Home-Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title","About-Smart Contact Manager");
		return "about";
	}
	@RequestMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("title","Register-Smart Contact Manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1,
			@RequestParam(value="agreement", defaultValue= "false") boolean agreement, Model model,
			HttpSession session)
	{
		
		
		try {
			if(!agreement)
			{
				System.out.println("You haven't agreed to the terms and condition");
				throw new Exception("You haven't agreed to the terms and condition");
			}
			if(result1.hasErrors())
			{
				System.out.println("Error"+result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRole("Role_User");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			User result=userRepository.save(user);
			System.out.println("agreement"+agreement);
			System.out.println("result"+ new User());
			session.setAttribute("message", new Message("Successfully Registered  ","Alert-Success" ));
			return "signup";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something Went Wrong !!  "+e.getMessage(),"Alert-danger" ));
			return "signup";
		}
		
	}

}
