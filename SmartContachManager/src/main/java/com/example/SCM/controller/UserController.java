package com.example.SCM.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.SCM.DAO.UserRepo;
import com.example.SCM.model.Contact;
import com.example.SCM.model.User;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepo userRepo;
	// method to adding common data to respose
	@ModelAttribute
	public void addCommonData(Model model,Principal principal)
	{
		String userName=principal.getName();
		
		User user=userRepo.getUserByUserName(userName);
		model.addAttribute(user);
	}
	// dashboard home
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal)
	{
		String userName=principal.getName();
		User user=userRepo.getUserByUserName(userName);
		model.addAttribute(user);
		
		
		return "normal/userDashboard";
	}
	//open add form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}
	
	// getting the data for add contact
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,Principal principal )
	{
		String name= principal.getName();
		User user=this.userRepo.getUserByUserName(name);
		contact.setUser(user);
		user.getContacts().add(contact);
		this.userRepo.save(user);
		System.out.println("Contact Added");
		return "normal/add_contact_form";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
