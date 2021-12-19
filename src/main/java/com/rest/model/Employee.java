package com.rest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_id", updatable = false, nullable = false)
	private int empId;
	@NonNull
	private String empName;
	@NonNull
	private String empDept;
	@NonNull
	private String empTech;

//	public Employee() {
//		super();
//	}
//
//	public Employee(int empId, String empName, String empDept, String empTech) {
//		super();
//		this.empId = empId;
//		this.empName = empName;
//		this.empDept = empDept;
//		this.empTech = empTech;
//	}

//	public int getEmpId() {
//		return empId;
//	}
//
//	public void setEmpId(int empId) {
//		this.empId = empId;
//	}
//
//	public String getEmpName() {
//		return empName;
//	}
//
//	public void setEmpName(String empName) {
//		this.empName = empName;
//	}
//
//	public String getEmpDept() {
//		return empDept;
//	}
//
//	public void setEmpDept(String empDept) {
//		this.empDept = empDept;
//	}
//
//	public String getEmpTech() {
//		return empTech;
//	}
//
//	public void setEmpTech(String empTech) {
//		this.empTech = empTech;
//	}
//
//	@Override
//	public String toString() {
//		return "Employee [empId=" + empId + ", empName=" + empName + ", empDept=" + empDept + ", empTech=" + empTech
//				+ "]";
//	}

}
