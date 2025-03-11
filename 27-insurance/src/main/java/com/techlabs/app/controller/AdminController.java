package com.techlabs.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.request.CityRequest;
import com.insurance.request.StateRequest;
import com.insurance.response.CityResponse;
import com.insurance.response.StateResponse;
import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.EmployeeResponseDto;
import com.techlabs.app.dto.TaxSettingRequestDto;
import com.techlabs.app.service.AdminService;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Add Employee")
	@PostMapping("addEmployee")
	public ResponseEntity<String> registerEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
		return new ResponseEntity<String>(adminService.registerEmployee(employeeRequestDto),
				HttpStatus.ACCEPTED);
	}
    
    @Operation(summary = "Add Agent")
	@PostMapping("addAgent")
	public ResponseEntity<String> registerAgent(@RequestBody AgentRequestDto agentRequestDto) {
		return new ResponseEntity<String>(adminService.registerAgent(agentRequestDto),
				HttpStatus.ACCEPTED);
	}
    
    

    
    @GetMapping
	public ResponseEntity<PagedResponse<AgentResponseDto>> getAllAgents(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "agentId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<AgentResponseDto> agents = adminService.getAllAgents(page, size, sortBy, direction);
		return new ResponseEntity<PagedResponse<AgentResponseDto>>(agents, HttpStatus.ACCEPTED);
	}
    
    @GetMapping("/getAllEmployees")
	public ResponseEntity<PagedResponse<EmployeeResponseDto>> getAllEmployees(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "employeeId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<EmployeeResponseDto> employees = adminService.getAllEmployees(page, size, sortBy, direction);
		return new ResponseEntity<PagedResponse<EmployeeResponseDto>>(employees, HttpStatus.ACCEPTED);
	}

    
    
    @PostMapping("/tax-setting")
    public ResponseEntity<String> createTaxSetting(@RequestBody TaxSettingRequestDto taxSettingRequestDto) {
      return new ResponseEntity<String>(adminService.createTaxSetting(taxSettingRequestDto),HttpStatus.CREATED);
      
    }
    
    
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<EmployeeResponseDto> viewEmployeebyId(@PathVariable(name = "employeeId") long employeeId) {
		return new ResponseEntity<EmployeeResponseDto>(adminService.findEmployeeByid(employeeId), HttpStatus.OK);
	}

    
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<AgentResponseDto> viewAgentById(@PathVariable(name = "agentId") long agentId) {
        return new ResponseEntity<AgentResponseDto>(adminService.findAgentById(agentId), HttpStatus.OK);
    }
    
    @PutMapping("/agent/{agentId}")
    public ResponseEntity<AgentResponseDto> updateAgentById(
            @PathVariable(name = "agentId") long agentId,
            @RequestBody AgentRequestDto agentRequestDto) {
        AgentResponseDto updatedAgent = adminService.updateAgentById(agentId, agentRequestDto);
        return new ResponseEntity<AgentResponseDto>(updatedAgent, HttpStatus.OK);
    }
    
    @PutMapping("/employee/{employeeId}")
    public ResponseEntity<EmployeeResponseDto> updateEmployeeById(
            @PathVariable(name = "employeeId") long employeeId,
            @RequestBody EmployeeRequestDto employeeRequestDto) {
        EmployeeResponseDto updatedEmployee = adminService.updateEmployeeById(employeeId, employeeRequestDto);
        return new ResponseEntity<EmployeeResponseDto>(updatedEmployee, HttpStatus.OK);
    }
    
    @PostMapping("/create-state") 
    public ResponseEntity<String> createState(@RequestBody StateRequest stateRequest){ 
      String response=adminService.createState(stateRequest); 
      return new ResponseEntity<>(response,HttpStatus.CREATED); 
       
    } 
    @GetMapping("/states") 
    public ResponseEntity<PagedResponse<StateResponse>> getAllStates( 
           @RequestParam(name = "page", defaultValue = "0") int page, 
           @RequestParam(name = "size", defaultValue = "5") int size, 
           @RequestParam(name = "sortBy", defaultValue = "stateId") String sortBy, 
           @RequestParam(name = "direction", defaultValue = "asc") String direction)  { 
         return new ResponseEntity<PagedResponse<StateResponse>>( 
             adminService.getAllStates(page, size, sortBy, direction), HttpStatus.OK); 
 
       } 
          
         @DeleteMapping("/state/{id}") 
         public ResponseEntity<String>deactivateState(@PathVariable(name="id") long id){ 
           return new ResponseEntity<String>(adminService.deactivateStateById(id),HttpStatus.OK); 
         } 
          
         @PutMapping("/state/{id}") 
         public ResponseEntity<String>activateState(@PathVariable(name="id") long id){ 
           return new ResponseEntity<String>(adminService.activateStateById(id),HttpStatus.OK); 
         } 
          
         @PostMapping("/create-city") 
         public ResponseEntity<String> createCity(@RequestBody CityRequest cityRequest){ 
  
          try { 
                 String response = adminService.createCity(cityRequest); 
                 return new ResponseEntity<>(response, HttpStatus.CREATED); 
             } catch (Exception e) { 
                 return new ResponseEntity<>("Error creating city: " + e.getMessage(), HttpStatus.BAD_REQUEST); 
             } 
            
         } 
          
         @DeleteMapping("/city/{id}") 
         public ResponseEntity<String>deactivateCity(@PathVariable(name="id") long id){ 
           return new ResponseEntity<String>(adminService.deactivateCity(id),HttpStatus.OK); 
            
         } 
          
         @GetMapping("/city/{id}") 
         public ResponseEntity<CityResponse>getCityById(@PathVariable(name="id") long id){ 
           return new ResponseEntity<CityResponse>(adminService.getCityById(id),HttpStatus.OK); 
         } 
          
         @PutMapping("/city/{id}") 
         public ResponseEntity<String>activateCity(@PathVariable(name="id") long id){ 
           return new ResponseEntity<String>(adminService.activateCity(id),HttpStatus.OK); 
         } 
          
         @GetMapping("/cities") 
         public ResponseEntity<PagedResponse<CityResponse>> getAllCities( 
           @RequestParam(name = "page", defaultValue = "0") int page, 
           @RequestParam(name = "size", defaultValue = "5") int size, 
           @RequestParam(name = "sortBy", defaultValue = "id") String sortBy, 
           @RequestParam(name = "direction", defaultValue = "asc") String direction){ 
          System.out.println("To get all cities"); 
           return new ResponseEntity<PagedResponse<CityResponse>>(adminService.getAllCities(page, size, sortBy, direction),HttpStatus.OK); 
            
            
         }




}