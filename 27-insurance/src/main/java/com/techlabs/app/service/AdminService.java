package com.techlabs.app.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.request.CityRequest;
import com.insurance.request.StateRequest;
import com.insurance.response.CityResponse;
import com.insurance.response.StateResponse;
import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.EmployeeResponseDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.dto.TaxSettingRequestDto;
import com.techlabs.app.util.PagedResponse;

public interface AdminService{
    String registerEmployee(EmployeeRequestDto employeeRequestDto);
	String registerAgent(AgentRequestDto agentRequestDto);
	String createTaxSetting(TaxSettingRequestDto taxSettingRequestDto);
	PagedResponse<AgentResponseDto> getAllAgents(int page, int size, String sortBy, String direction);
	PagedResponse<EmployeeResponseDto> getAllEmployees(int page, int size, String sortBy, String direction);
	EmployeeResponseDto findEmployeeByid(long id);
	AgentResponseDto findAgentById(long agentId);
	AgentResponseDto updateAgentById(long agentId, AgentRequestDto agentRequestDto);
	EmployeeResponseDto updateEmployeeById(long employeeId, EmployeeRequestDto employeeRequestDto);
	PagedResponse<CityResponse> getAllCities(int page, int size, String sortBy, String direction);
	String activateCity(long id);
	CityResponse getCityById(long id);
	String deactivateCity(long id);
	String createCity(CityRequest cityRequest);
	String activateStateById(long id);
	String deactivateStateById(long id);
	PagedResponse<StateResponse> getAllStates(int page, int size, String sortBy, String direction);
	String createState(StateRequest stateRequest);
}