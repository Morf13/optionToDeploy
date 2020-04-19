package com.hello.opa.repos;


import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.hello.opa.domain.Role;
import com.hello.opa.domain.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByUsername(String username);

	User findByActivationCode(String code);
	
	List<User> findAllByRoles(Role role);

	User findById(long id);



	

}