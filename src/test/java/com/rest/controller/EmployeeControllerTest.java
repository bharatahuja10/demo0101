package com.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.model.Employee;
import com.rest.repo.EmployeeRepo;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

//	@Test
//	void test() {
//		fail("Not yet implemented");
//	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private EmployeeRepo empRepo;

	private TestInfo testInfo;
	private TestReporter testReporter;

	@BeforeEach
	void init(TestInfo testInfo, TestReporter testReporter) {
		this.testInfo = testInfo;
		this.testReporter = testReporter;
	}

	@Test
	@DisplayName("Get ALL Employees")
	public void testAllEmployeeList() throws Exception {
		List<Employee> empList = new ArrayList<>();
		empList.add(new Employee(1, "Abhinav", "Programmer", "Python"));
		empList.add(new Employee(2, "Bhauvik", "Programmer", "Python"));
		empList.add(new Employee(3, "Chaitanya", "Programmer", "Python"));
		empList.add(new Employee(4, "Dheeraj", "Programmer", "Python"));

		Mockito.when(empRepo.findAll()).thenReturn(empList);

		String url = "/employees";

		MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();
		String actualJsonResponse = mvcResult.getResponse().getContentAsString();
		testReporter.publishEntry("Actual: " + actualJsonResponse);

		String expectedJsonResponse = objectMapper.writeValueAsString(empList);
		testReporter.publishEntry("Expected: " + expectedJsonResponse);

		assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
	}

	@Test
	@DisplayName("Get Employee By ID")
	public void testGetEmployeeById() throws Exception {
		Optional<Employee> employee = Optional.of(new Employee(1, "Abhinav", "Programming", "Python"));
		String employeeId = "1";

		Mockito.when(empRepo.findById(1)).thenReturn(employee);

		String url = "/employee/" + employeeId;

		MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();
		String actualJsonResponse = mvcResult.getResponse().getContentAsString();
		testReporter.publishEntry("Actual: " + actualJsonResponse);

		String expectedJsonResponse = objectMapper.writeValueAsString(employee);
		testReporter.publishEntry("Expected: " + expectedJsonResponse);

		assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
	}
	
	//Testing POST Employee Method
	@Test
	@DisplayName("Create New Employee")
	public void testCreateNewEmployee() throws Exception {
		Employee newEmp = new Employee();
		newEmp.setEmpName("Rohit");
		newEmp.setEmpDept("Production");
		newEmp.setEmpTech("Python");
		
		Employee savedEmp = new Employee(1,"Rohit","Production","Python");
		
		Mockito.when(empRepo.save(newEmp)).thenReturn(savedEmp);
//		Mockito.when(empRepo.save(any(Employee.class))).thenReturn(savedEmp);

		
		String url = "/employee";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(newEmp))
				).andExpect(status().isOk())
				.andExpect(content().json("1"));
	}
	
	@Test
	@DisplayName("Update Employee")
	public void testUpdateEmployee() throws Exception {
		Integer empId = 1;
		
		Optional<Employee> existingEmp = Optional.of(new Employee(empId,"Rohit","Production","Python"));
		
		Employee updateEmp = new Employee(empId,"Rohit Rao","Production","Python");
		
		Mockito.when(empRepo.findById(empId)).thenReturn(existingEmp);
		
		//Don't use Mockito.when(empRepo.save(newEmp) as it will return null ID in EmployeeController.java -> https://mkyong.com/spring-boot/spring-mockito-unable-to-mock-save-method/ 
		//Either do as done in testCreateNewEmployee() or implement equals and hashCode in the Employee model
		//Generated Equals and Hashcode for the Employee model
		Mockito.when(empRepo.save(updateEmp)).thenReturn(updateEmp);
		
		String url = "/employee/"+empId;
		
		MvcResult mvcResult = mockMvc.perform(
				put(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(updateEmp))
				).andExpect(status().isOk()).andReturn();
		String actualJsonResponse = mvcResult.getResponse().getContentAsString();
		
		testReporter.publishEntry("Actual: " + actualJsonResponse);

		String expectedJsonResponse = objectMapper.writeValueAsString(updateEmp);
		testReporter.publishEntry("Expected: " + expectedJsonResponse);

		assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
	}
	
	@Test
	@DisplayName("Delete Employee")
	public void testDeleteEmployee() throws Exception {
		Integer empId = 1;
		Employee emp = new Employee(empId,"Rohit","Production","Python");
		
		Mockito.when(empRepo.findById(1)).thenReturn(Optional.of(emp));
		
		Mockito.doNothing().when(empRepo).delete(emp);
		
		String url = "/employee/"+empId;
		
		mockMvc.perform(delete(url)).andExpect(status().isOk());
		
		Mockito.verify(empRepo, times(1)).delete(emp);
	}
}
//