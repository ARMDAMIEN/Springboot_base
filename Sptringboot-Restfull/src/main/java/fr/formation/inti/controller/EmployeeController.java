package fr.formation.inti.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import fr.formation.inti.dao.EmployeDao;
import fr.formation.inti.entity.Employee;

@RestController
@RequestMapping("/api")
public class EmployeeController {

	
	@Autowired
	private EmployeDao employeeDao;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public String index() {
		
		return "Hello webService Restful";
	}
	
	@GetMapping("/employee/{empId}")
	public ResponseEntity<Employee> getEmployee(@PathVariable Integer empId) {
		
		
		Employee emp = employeeDao.findById(empId).get();
		return new ResponseEntity<Employee>(emp,HttpStatus.OK);
	}
	
	@GetMapping("/all/")
	public ResponseEntity<?> getAllEmployee(){
		
		List<Employee> employees = employeeDao.findAll();
		return new ResponseEntity<List<Employee>>(employees,HttpStatus.OK);
		
	}
	
	@PostMapping("/employee/")
	public ResponseEntity<?> addEmployee(@RequestBody Employee emp, UriComponentsBuilder ucBuilder){
		
		
		employeeDao.save(emp);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("api/employee/{empId}").buildAndExpand(emp.getEmpId()).toUri());
		return new ResponseEntity<String>(headers,HttpStatus.CREATED);
		
	}
	
	/**
	 * PUT : update 
	 * @return
	 */
	@PutMapping("/employee/{empId}")
	public ResponseEntity<?> addEmployee(@PathVariable Integer empId, @RequestBody Employee emp){
		Optional<Employee> optionalEmp = employeeDao.findById(empId);
		if(!optionalEmp.isPresent())
			return new ResponseEntity<String>(new String("Employee id "+empId+" not found"), HttpStatus.NOT_FOUND);
		
		Employee currentEmp = optionalEmp.get();
		currentEmp.setFirstName(emp.getFirstName());
		currentEmp.setLastName(emp.getLastName());
		currentEmp.setStartDate(emp.getStartDate());
		currentEmp.setTitle(emp.getTitle());
		currentEmp.setDepartment(emp.getDepartment());
		currentEmp.setEndDate(emp.getEndDate());
		
		employeeDao.save(currentEmp);
		
		return new ResponseEntity<Employee>(currentEmp,HttpStatus.OK);
	}
	
	/**
	 * DELETE : delete
	 * @param empId
	 * @return
	 */
	@DeleteMapping("/employee/{empId}")	
	public ResponseEntity<?> deleteEmployee(@PathVariable Integer empId) {
		Optional<Employee> optionalEmp = employeeDao.findById(empId);	
		if(!optionalEmp.isPresent())
			return new ResponseEntity<String>(new String("Employee id "+empId+" not found"), HttpStatus.NOT_FOUND);
		employeeDao.deleteById(empId);
		return new ResponseEntity<Employee>(optionalEmp.get(),HttpStatus.NO_CONTENT);
	}

}
