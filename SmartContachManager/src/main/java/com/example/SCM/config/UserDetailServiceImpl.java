package com.example.SCM.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.SCM.DAO.UserRepo;
import com.example.SCM.model.User;

public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepo.getUserByUserName(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("User name is invalid");
		}
		CustomDetails customDetails=new CustomDetails(user);
		return customDetails;
	}

}
