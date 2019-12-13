package com.scholanova.projectstore.models;

public class Stock {
	
	private Integer id;
	private Integer id_store;
	private String name;
	private String type;
	private Integer value;
	
	public Stock() {}
	
	public Stock(Integer id, Integer id_store, String name, String type, Integer value) {
		super();
		this.id = id;
		this.id_store = id_store;
		this.name = name;
		this.type = type;
		this.value = value;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId_store() {
		return id_store;
	}
	public void setId_store(Integer id_store) {
		this.id_store = id_store;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	

}
