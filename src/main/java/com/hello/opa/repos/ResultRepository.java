package com.hello.opa.repos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.hello.opa.domain.Result;


public interface ResultRepository extends CrudRepository <Result, Long> {
	
	@Query(value = "SELECT * FROM RESULTS WHERE USER_ID = :user_id ORDER BY modtime DESC", nativeQuery = true)
	List<Result> findAllByUser_id(long user_id);
	

}
