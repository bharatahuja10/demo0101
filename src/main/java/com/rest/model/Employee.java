package com.rest.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_id", updatable = false, nullable = false)
	private int empId;
	private String empName;
	private String empDept;
	private String empTech;

	public Employee() {
		super();
	}

	public Employee(int empId, String empName, String empDept, String empTech) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.empDept = empDept;
		this.empTech = empTech;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpDept() {
		return empDept;
	}

	public void setEmpDept(String empDept) {
		this.empDept = empDept;
	}

	public String getEmpTech() {
		return empTech;
	}

	public void setEmpTech(String empTech) {
		this.empTech = empTech;
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName + ", empDept=" + empDept + ", empTech=" + empTech
				+ "]";
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(empDept, empId, empName, empTech);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(empDept, other.empDept) && empId == other.empId && Objects.equals(empName, other.empName)
				&& Objects.equals(empTech, other.empTech);
	}
	
	
}
