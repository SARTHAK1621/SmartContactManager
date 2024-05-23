package com.example.SCM.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SCM.DAO.UserRepo;
import com.example.SCM.helper.Message;
import com.example.SCM.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {
	Random random=new Random(1000);
	int generatedOTP;
	String Email;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepo userRepo;
	//emailid form open handler
	@RequestMapping("/forgot")
	public String openEmailForm()
	{
		return "forgot_email_form";
	}
	@PostMapping("/send-OTP")
	public String sendOTP(@RequestParam("email") String email)
	{
		System.out.println(email);
		// generating otp
		Email=email;
		generatedOTP=random.nextInt(9999);
		System.out.println(generatedOTP);
		return "verify_OTP";
	}
	@PostMapping("/verify-OTP")
	public String verifyOTP(@RequestParam("otp") int recivedOTP, HttpSession session)
	{
		//System.out.println(recivedOTP);
		// generating otp
		if(recivedOTP==generatedOTP)
		{
			//System.out.println("It is a match");
			//System.out.println(Email);
			User user=this.userRepo.getUserByUserName(Email);
			//System.out.println(user.getEmail());
			session.setAttribute("message", new Message("Your Entered OTP is CORRECT ENTER YOUR NEW PASSWORD","success"));
			//String newPassword=this.bCryptPasswordEncoder.encode())
			return "new_password";
			
		}
		else
		{
			//System.out.println("It is not a match");
			session.setAttribute("message", new Message("Your Entered OTP is wrong","danger"));
			return "redirect:/forgot";
		}
		
		
		
	}
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("password1") String password1,@RequestParam("password2") String password2, HttpSession session)
	{
		//System.out.println(password1.getClass());
		System.out.println(password1);
		System.out.println(password2);
		if(password1.equals(password2))
		{
			System.out.println("We are good to change the password");
			User user=this.userRepo.getUserByUserName(Email);
			String newPassword=this.bCryptPasswordEncoder.encode(password1);
			user.setPassword(newPassword);
			this.userRepo.save(user);
			
			
		}
		else
		{
			System.out.println("We are not good to change the password");
			session.setAttribute("message", new Message("ENTER YOUR PASSWORD PROPERLY","danger"));
			return "redirect:/signin";
		}
		session.setAttribute("message", new Message("Your PASSWORD IS CHANGED","success"));
		return "redirect:/signin";
	}
	

}
