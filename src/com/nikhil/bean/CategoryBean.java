package com.nikhil.bean;

public class CategoryBean implements java.io.Serializable{

	private String id, name, description;
	private static final long serialVersionUID = 1L;

	public CategoryBean(){
		
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
