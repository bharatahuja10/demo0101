package com.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.model.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

}
