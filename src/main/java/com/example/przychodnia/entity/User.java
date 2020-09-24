package com.example.przychodnia.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;

@Entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(max = 24, message = "nazwa użytkownika nie może być dłuższa niż 24 znaki")
	@Size(min = 3, message = "podaj nazwę użytkownika")
	private String userName;
	@Size(min = 8, message = "hasło musi się składać z conajmniej 8 znaków")
	private String password;
	@Pattern(regexp = "^(.+)@(.+)$", message = "email nie prawidłowy")
	private String email;
	@Size(min = 1, message = "podaj imię")
	private String firstName;
	@Size(min = 1, message = "podaj nazwisko")
	private String lastName;
	@Pattern(regexp = "\\d{11}", message = "pesel powinien składać się z 11 cyfr")
	private String personalIdentityNumber;
	@Size(min = 1, message = "podaj miejscowość")
	private String city;
	private String street;
	@Size(min = 1, message = "podaj numer lokalu")
	private String houseNumber;
	private String flatNumber;
	@Pattern(regexp = "\\d{2}-\\d{3}", message = "podaj kod pocztowy")
	private String postalCode;
	@Size(min = 5, message = "podaj numer telefonu")
	private String phone;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private List<Role> roles;
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Message> messages;
}
