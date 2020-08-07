package com.hello.opa.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "exercises")

public class Exercise {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "Please add the Title")
	@Length(max = 255, message = "Title too long")

	private String title;
	
	@NotBlank(message = "Please add the task")
	private String task;
	
	@NotBlank(message = "Please add the topic")
	private String topic;
	
	@NotBlank(message = "Please add the explanation")
	private String explanation;
	
	@NotBlank(message = "Please choose Exercise type")
	private String typeOfTask;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User author;
	
	@ManyToMany(cascade = CascadeType.ALL)
	 @JoinTable(name = "pupil_exercises", joinColumns = { @JoinColumn(name = "user_id") }, 
     inverseJoinColumns = { @JoinColumn(name = "exercise_id") })
    private Set<User> pupils;

	public Exercise() {

	}

	public Exercise(String title, User author) {
		this.title = title;
		this.author = author;

	}

	public String getTitle() {
		return title;
	}

	public String getAuthorName() {
		return author != null ? author.getUsername() : "<none>";
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String fileName) {
		this.task = fileName;
	}

	public String getType() {
		return typeOfTask;
	}

	public void setType(String type) {
		this.typeOfTask = type;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getTypeOfTask() {
		return typeOfTask;
	}

	public void setTypeOfTask(String typeOfTask) {
		this.typeOfTask = typeOfTask;
	}

	public Set<User> getPupils() {
		return pupils;
	}

	public void setPupils(Set<User> pupils) {
		this.pupils = pupils;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exercise other = (Exercise) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
	
}
