package com.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
	public void testCreateNewEmployee() throws Exception {
		Employee newEmp = new Employee();
		newEmp.setEmpName("Rohit");
		newEmp.setEmpDept("Production");
		newEmp.setEmpTech("Python");
		
		Employee savedEmp = new Employee(1,"Rohit","Production","Python");
		
		//USE LOMBOK LIBRARY TO IMPLEMENT EQUALS AND HASHCODE METHOD AUTOMATICALLY -> https://stackabuse.com/guide-to-unit-testing-spring-boot-rest-apis/
		//Don't use Mockito.when(empRepo.save(newEmp) as it will return null ID in EmployeeController.java -> https://mkyong.com/spring-boot/spring-mockito-unable-to-mock-save-method/ 
//		Mockito.when(empRepo.save(newEmp)).thenReturn(savedEmp);
		Mockito.when(empRepo.save(any(Employee.class))).thenReturn(savedEmp);

		
		String url = "/employee";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(newEmp))
				).andExpect(status().isOk())
				.andExpect(content().json("1"));
//		String actualJsonResponse = mvcResult.getResponse().getContentAsString();
		
//		String expectedJsonResponse = "1";
//		assertEquals(expectedJsonResponse, actualJsonResponse);
//		System.out.println(actualJsonResponse);
	}
}
//