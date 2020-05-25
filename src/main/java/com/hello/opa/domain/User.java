package com.hello.opa.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "usr")
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotBlank(message = "Enter username")
	private String username;
	@NotBlank(message = "Enter password")
	private String password;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Exercise> exercises;
	private String activationCode;

	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
	@Enumerated(EnumType.STRING)
	private Set<Role> roles;
	
	@ManyToMany(targetEntity = Exercise.class, cascade = {CascadeType.ALL})
    @JoinTable(name = "pupil_exercises", joinColumns = { @JoinColumn(name = "user_id") }, 
                       inverseJoinColumns = { @JoinColumn(name = "exercise_id") })
    private Set<Exercise> exercisesTodo;
	
	@ManyToMany(targetEntity = User.class, cascade = {CascadeType.ALL})
    @JoinTable(name = "pupil_teacher", joinColumns = { @JoinColumn(name = "teacher_id") }, 
                       inverseJoinColumns = { @JoinColumn(name = "pupil_id") })
    private Set<User> pupils;
	
	@ManyToMany(targetEntity = User.class, cascade = {CascadeType.DETACH})
    @JoinTable(name = "teacher_pupil", joinColumns = { @JoinColumn(name = "pupil_id") }, 
                       inverseJoinColumns = { @JoinColumn(name = "teacher_id") })
    private Set<User> teachers;
	

	private boolean active;

	@Email(message = "Email is not correct")
	@NotBlank(message = "Enter email")
	private String email;
	
	public User() {

	}
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
	

	public boolean isAdmin() {
		return roles.contains(Role.ADMIN);
	}
	public Set<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(Set<Exercise> exercises) {
		this.exercises = exercises;
	}
	public boolean isTeacher() {
		return roles.contains(Role.TEACHER);
	}
	

	public Long getId() {
		return id;
	}

	

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isActive();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getRoles();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	public Set<Exercise> getExercisesTodo() {
		return exercisesTodo;
	}
	public void setExercisesTodo(Set<Exercise> exercisesTodo) {
		this.exercisesTodo = exercisesTodo;
	}
	public Set<User> getPupils() {
		return pupils;
	}
	public void setPupils(Set<User> pupils) {
		this.pupils = pupils;
	}
	public Set<User> getTeachers() {
		return teachers;
	}
	public void setTeachers(Set<User> teachers) {
		this.teachers = teachers;
	}
	

}