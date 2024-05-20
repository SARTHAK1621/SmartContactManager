package com.example.SCM.DAO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.SCM.model.Contact;
import com.example.SCM.model.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Integer> {
	// pagination...
	@Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
	public Page<Contact> findContactByUser(@Param("userId")int userId, Pageable pegable );
	
	
	//search query
	List<Contact> findByFirstnameContainingAndUser(String firstname, User user);

}
