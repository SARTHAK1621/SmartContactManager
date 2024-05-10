package com.example.SCM.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.SCM.DAO.UserRepo;
import com.example.SCM.helper.Message;
import com.example.SCM.model.Contact;
import com.example.SCM.model.User;

import jakarta.servlet.http.HttpSession;

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
	public String processContact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session )
	{
		try {
			String name= principal.getName();
			User user=this.userRepo.getUserByUserName(name);
			// processing and uploading file
			if(file.isEmpty())
			{
				System.out.println("There is no file");
			}
			else
				
			{
				System.out.println("We have a image file");
				// upload the file to folder and update the name to contact
				contact.setImage(file.getOriginalFilename());
				File saveFile=new ClassPathResource("static/img").getFile();
				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image got uploaded");
			}
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepo.save(user);
			System.out.println("Contact Added");
			// message success .....
			session.setAttribute("message", new Message("Your Contact is added", "success"));
			
		}
		catch(Exception e)
		{
			System.out.println("Error"+e.getMessage());
			e.printStackTrace();
			// error message
			session.setAttribute("message", new Message("Your Contact is not added", "danger"));
		}
		return "normal/add_contact_form";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
