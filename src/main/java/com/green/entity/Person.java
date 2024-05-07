package com.green.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Person {
	private Long id;
	private String name;
	private int age;
	private List<String> hobbies;
}
