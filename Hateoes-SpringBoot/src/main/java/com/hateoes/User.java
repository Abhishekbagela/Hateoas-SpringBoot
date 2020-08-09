package com.hateoes;

import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "All details about the user")
@Entity(name = "user")
public class User implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Size(min = 5)
	@ApiModelProperty(notes = "name should have atleast 5 characters")
	private String name;

	@Past
	@ApiModelProperty(notes = "Birth date should be in the past")
	private Date dob;

	private String msg;

	public User() {
	}

	public User(@Size(min = 5) String name, @Past Date dob, String msg) {
		super();
		this.name = name;
		this.dob = dob;
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", dob=" + dob + ", msg=" + msg + "]";
	}

}
