package com.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.model.Employee;
import com.rest.model.Employee.EmployeeBuilder;
import com.rest.repo.EmployeeRepo;
import lombok.*;

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
	
	//Testing GET Methods
	@Test
	public void testAllEmployeeList() throws Exception {
		List<Employee> empList = new ArrayList<>();
		empList.add(new Employee());
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

		Mockito.when(empRepo.findById(Integer.parseInt(employeeId))).thenReturn(employee);

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
		Employee newEmp = Employee.builder().empId(1).empName("Rohit").empDept("Production").empTech("Python").build();
//		Employee newEmp = new Employee();
//		newEmp.setEmpName("Rohit");
//		newEmp.setEmpDept("Production");
//		newEmp.setEmpTech("Python");
		
//		Employee savedEmp = new Employee(1,"Rohit","Production","Python");
		
		//USE LOMBOK LIBRARY TO IMPLEMENT EQUALS AND HASHCODE METHOD AUTOMATICALLY -> https://stackabuse.com/guide-to-unit-testing-spring-boot-rest-apis/
		//Don't use Mockito.when(empRepo.save(newEmp) as it will return null ID in EmployeeController.java -> https://mkyong.com/spring-boot/spring-mockito-unable-to-mock-save-method/ 
//		Mockito.when(empRepo.save(any(Employee.class))).thenReturn(savedEmp);
		Mockito.when(empRepo.save(newEmp)).thenReturn(newEmp);
		
		String url = "/employee";
		MvcResult mvcResult = mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(newEmp))
				).andExpect(status().isOk()).andReturn();
//				.andExpect(content().json("1"));
		String actualJsonResponse = mvcResult.getResponse().getContentAsString();
		
		String expectedJsonResponse = "1";
		assertEquals(expectedJsonResponse, actualJsonResponse);
//		System.out.println(actualJsonResponse);
	}
	
	//Testing PUT Employee Method
	@Test
	public void testUpdateEmployee() throws Exception {
		Integer empId = 1;
		
		Optional<Employee> existingEmp = Optional.of(new Employee(empId,"Rohit","Production","Python"));
		
		Employee updateEmp = Employee.builder().empId(empId).empName("Rohit Manohar").empDept("Production").empTech("Python").build();
		
		Mockito.when(empRepo.findById(empId)).thenReturn(existingEmp);
		//USE LOMBOK LIBRARY TO IMPLEMENT EQUALS AND HASHCODE METHOD AUTOMATICALLY
		//Don't use Mockito.when(empRepo.save(newEmp) as it will return null ID in EmployeeController.java -> https://mkyong.com/spring-boot/spring-mockito-unable-to-mock-save-method/ 
		//Either do as done in testCreateNewEmployee() or implement equals and hashCode in the Employee model
		Mockito.when(empRepo.save(updateEmp)).thenReturn(updateEmp);
		
		String url = "/employee/"+empId;
		
		System.out.println(existingEmp.get().getEmpName());
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(url)
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(updateEmp));
		
//		MvcResult mvcResult = mockMvc.perform(
//				put(url)
//				.contentType("application/json")
//				.content(objectMapper.writeValueAsString(updateEmp))
//				).andExpect(status().isOk()).andReturn();
//		String actualJsonResponse = mvcResult.getResponse().getContentAsString();
//		System.out.println(existingEmp.get().getEmpName());
//		System.out.println(updateEmp.getEmpName());
//		System.out.println("HERE: "+actualJsonResponse);
		mockMvc.perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.empName", is("Rohit Manohar")));
//		String actualJsonResponse = mvcResult.getResponse().getContentAsString();
//		System.out.println(actualJsonResponse);
//		testReporter.publishEntry("Actual:"+actualJsonResponse);
	}
	
	@Test
	public void testDeleteEmployee() throws Exception {
		Integer empId = 1;
		Employee emp = Employee.builder().empId(empId).empName("Rohit Manohar").empDept("Production").empTech("Python").build();
		
		Mockito.when(empRepo.findById(empId)).thenReturn(Optional.of(emp));
		Mockito.doNothing().when(empRepo).delete(emp);
		
		String url = "/employee/"+empId;
		
		mockMvc.perform(delete(url)).andExpect(status().isOk());
		
		Mockito.verify(empRepo, times(1)).delete(emp);
	}
}
