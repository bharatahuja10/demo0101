package com.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rest.exception.IncompleteEmployeeDataException;
import com.rest.exception.ResourceNotFoundException;
import com.rest.model.Employee;
import com.rest.repo.EmployeeRepo;
import com.rest.static_strings.StaticStrings;

@RestController
public class EmployeeController {

	@Autowired
	EmployeeRepo empRepo;

	@GetMapping(path = "/employees", produces = { "application/json", "application/xml" })
	public List<Employee> getEmployees() {
		return this.empRepo.findAll();
	}

	@GetMapping("/employee/{id}")
	public Employee getEmployee(@PathVariable("id") int id) {

		return this.empRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(StaticStrings.Errors.errorNotFound(id)));
	}

	@PostMapping(path = "/employee", produces = {"application/json", "application/xml"}, consumes = { "application/json" })
	public String postEmployee(@RequestBody Employee emp) { // NULL ERROR in db: Use Request Body
		if (emp.getEmpName() == null || emp.getEmpDept() == null || emp.getEmpTech() == null) {
//			return ResponseEntity.badRequest().build();
			throw new IncompleteEmployeeDataException(StaticStrings.Errors.errorEmpDetails);
		}
		System.out.println("HERE->" + emp.getEmpDept());
		System.out.println(emp.getEmpDept() == null);
		System.out.println(emp.getEmpDept());
		Employee savedEmp = this.empRepo.save(emp);
		return String.valueOf(savedEmp.getEmpId());
	}

//	@DeleteMapping("/employee/{id}")
//	public String deleteEmployee(@PathVariable("id") int id) {
//		Employee emp = empRepo.getById(id);
//		this.empRepo.delete(emp);
//		return "Successfully Deleted";
//	}

	@DeleteMapping("/employee/{id}")
	public ResponseEntity<Employee> deleteEmployee(@PathVariable("id") int id) {
		Employee existingEmp = this.empRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(StaticStrings.Errors.errorNotFound(id)));
		this.empRepo.delete(existingEmp);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/employee/{id}")
	public Employee saveOrUpdateEmployee(@RequestBody Employee emp, @PathVariable("id") int id) {
		Employee existingEmp = this.empRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(StaticStrings.Errors.errorNotFound(id)));
		existingEmp.setEmpName(emp.getEmpName());
		existingEmp.setEmpDept(emp.getEmpDept());
		existingEmp.setEmpTech(emp.getEmpTech());
		return this.empRepo.save(existingEmp);
	}
}
