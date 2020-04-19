package com.hello.opa.repos;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hello.opa.domain.Exercise;
import com.hello.opa.domain.User;


public interface ExerciseRepository extends CrudRepository <Exercise, Long> {
	
	Page<Exercise> findAll(Pageable pageable);
	@Query("from Exercise e where e.author = :author")
	Page<Exercise> findByUser(Pageable pageable, @Param("author") User author);
	Exercise findById(long id);


}
