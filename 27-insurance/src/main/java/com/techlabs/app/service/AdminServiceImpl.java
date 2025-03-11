package com.techlabs.app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insurance.request.CityRequest;
import com.insurance.request.StateRequest;
import com.insurance.response.CityResponse;
import com.insurance.response.StateResponse;
import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.CityResponseDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.EmployeeResponseDto;
import com.techlabs.app.dto.StateResponseDto;
import com.techlabs.app.dto.TaxSettingRequestDto;
import com.techlabs.app.dto.UserResponseDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.State;
import com.techlabs.app.entity.TaxSetting;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.BankApiException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.EmployeeRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.StateRepository;
import com.techlabs.app.repository.TaxSettingRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.util.PagedResponse;

@Service
public class AdminServiceImpl implements AdminService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private EmployeeRepository employeeRepository;
    private AgentRepository agentRepository;
    private CityRepository cityRepository;
    private TaxSettingRepository taxSettingRepository;
    private StateRepository stateRepository;


    


	

	public AdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository, AgentRepository agentRepository,
			CityRepository cityRepository, TaxSettingRepository taxSettingRepository, StateRepository stateRepository) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.employeeRepository = employeeRepository;
		this.agentRepository = agentRepository;
		this.cityRepository = cityRepository;
		this.taxSettingRepository = taxSettingRepository;
		this.stateRepository = stateRepository;
	}







	@Transactional
    @Override
    public String registerEmployee(EmployeeRequestDto employeeRequestDto) {

    	
    	if (userRepository.existsByUsername(employeeRequestDto.getUsername())) {
            throw new BankApiException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }
        if (userRepository.existsByEmail(employeeRequestDto.getEmail())) {
            throw new BankApiException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }
        
        

        User user = new User();
        user.setUsername(employeeRequestDto.getUsername());
        user.setEmail(employeeRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(employeeRequestDto.getPassword()));

        Role employeeRole = roleRepository.findByName("ROLE_EMPLOYEE")
                .orElseThrow(() -> new BankApiException(HttpStatus.BAD_REQUEST, "Role not found: ROLE_EMPLOYEE"));
        Set<Role> roles = new HashSet<>();
        roles.add(employeeRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        Employee employee = new Employee();
        employee.setUser(savedUser);
        employee.setName(employeeRequestDto.getName());
        employee.setActive(employeeRequestDto.isActive());

        employeeRepository.save(employee);
		return "Employee registered successfully";
    }





	

    @Transactional
    @Override
    public String registerAgent(AgentRequestDto agentRequestDto) {
        if (userRepository.existsByUsername(agentRequestDto.getUsername())) {
            throw new BankApiException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }
        if (userRepository.existsByEmail(agentRequestDto.getEmail())) {
            throw new BankApiException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }

        User user = new User();
        user.setUsername(agentRequestDto.getUsername());
        user.setEmail(agentRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(agentRequestDto.getPassword()));

        Role agentRole = roleRepository.findByName("ROLE_AGENT")
                .orElseThrow(() -> new BankApiException(HttpStatus.BAD_REQUEST, "Role not found: ROLE_AGENT"));
        Set<Role> roles = new HashSet<>();
        roles.add(agentRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        City city = cityRepository.findById(agentRequestDto.getCity_id())
                .orElseThrow(() -> new BankApiException(HttpStatus.BAD_REQUEST, "City not found with id: " + agentRequestDto.getCity_id()));

        Agent agent = new Agent();
        agent.setUser(savedUser);
        agent.setName(agentRequestDto.getName());
        agent.setPhoneNumber(agentRequestDto.getPhoneNumber());
        agent.setCity(city);  // Set the fetched City object
        agent.setActive(agentRequestDto.isActive());
        agentRepository.save(agent);
        return "Agent Registered successfully";
    }
    
    

  @Override
  public String createTaxSetting(TaxSettingRequestDto taxSettingRequestDto) {
    TaxSetting taxSetting=new TaxSetting();
    taxSetting.setTaxPercentage(taxSettingRequestDto.getTaxPercentage());
    taxSetting.setUpdatedAt(taxSettingRequestDto.getUpdatedAt());
    taxSettingRepository.save(taxSetting);
    return "Tax Setting updated";
  }



  @Override
  public PagedResponse<AgentResponseDto> getAllAgents(int page, int size, String sortBy, String direction) {
      Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) 
          ? Sort.by(sortBy).ascending() 
          : Sort.by(sortBy).descending();

      Pageable pageable = PageRequest.of(page, size, sort);

      Page<Agent> agentsPage = agentRepository.findAll(pageable);

      List<AgentResponseDto> agentDtos = agentsPage.getContent().stream()
              .map(this::convertAgentToAgentResponseDto)
              .collect(Collectors.toList());

      return new PagedResponse<>(
              agentDtos,
              agentsPage.getNumber(),
              agentsPage.getSize(),
              agentsPage.getTotalElements(),
              agentsPage.getTotalPages(),
              agentsPage.isLast()
      );
  }

  private AgentResponseDto convertAgentToAgentResponseDto(Agent agent) {
      AgentResponseDto agentDto = new AgentResponseDto();
      
      agentDto.setAgentId(agent.getAgentId());
      agentDto.setName(agent.getName());
      agentDto.setPhoneNumber(agent.getPhoneNumber());
      agentDto.setActive(agent.isActive());

      agentDto.setCity(convertCityToCityResponseDto(agent.getCity()));

      UserResponseDto userDto = new UserResponseDto();
      userDto.setId(agent.getUser().getId());
      userDto.setUsername(agent.getUser().getUsername());
      userDto.setEmail(agent.getUser().getEmail());
      agentDto.setUserResponseDto(userDto);


      return agentDto;
  }

  private CityResponseDto convertCityToCityResponseDto(City city) {
      CityResponseDto cityDto = new CityResponseDto();
      cityDto.setId(city.getId());
      cityDto.setCityName(city.getCity_name());

      cityDto.setState(convertStateToStateResponseDto(city.getState()));

      return cityDto;
  }

  private StateResponseDto convertStateToStateResponseDto(State state) {
      StateResponseDto stateDto = new StateResponseDto();
      stateDto.setStateId(state.getStateId());
      stateDto.setName(state.getName());

  

      return stateDto;
  }
  
  
  @Override
  public PagedResponse<EmployeeResponseDto> getAllEmployees(int page, int size, String sortBy, String direction) {
      Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) 
          ? Sort.by(sortBy).ascending() 
          : Sort.by(sortBy).descending();

      Pageable pageable = PageRequest.of(page, size, sort);

      Page<Employee> employeesPage = employeeRepository.findAll(pageable);

      List<EmployeeResponseDto> employeeDtos = employeesPage.getContent().stream()
              .map(this::convertEmployeeToEmployeeResponseDto)
              .collect(Collectors.toList());

      return new PagedResponse<>(
              employeeDtos,
              employeesPage.getNumber(),
              employeesPage.getSize(),
              employeesPage.getTotalElements(),
              employeesPage.getTotalPages(),
              employeesPage.isLast()
      );
  }

  private EmployeeResponseDto convertEmployeeToEmployeeResponseDto(Employee employee) {
      EmployeeResponseDto employeeDto = new EmployeeResponseDto();

      employeeDto.setEmployeeId(employee.getEmployeeId());
      employeeDto.setName(employee.getName());
      employeeDto.setActive(employee.isActive());

      employeeDto.setUserId(employee.getUser().getId());
      employeeDto.setUsername(employee.getUser().getUsername());
      employeeDto.setEmail(employee.getUser().getEmail());

      return employeeDto;
  }



@Override
public EmployeeResponseDto findEmployeeByid(long id) {
	 Employee employee = employeeRepository.findById(id)
             .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

     return convertEmployeeToEmployeeResponseDto(employee);
}



@Override
public AgentResponseDto findAgentById(long agentId) {
	 Agent agent = agentRepository.findById(agentId)
	            .orElseThrow(() -> new RuntimeException("Agent not found with id: " + agentId));

	    return convertAgentToAgentResponseDto(agent);	
}

@Override
public AgentResponseDto updateAgentById(long agentId, AgentRequestDto agentRequestDto) {
	Agent existingAgent = agentRepository.findById(agentId)
            .orElseThrow(() -> new RuntimeException("Agent not found with id: " + agentId));

    AgentResponseDto agentDto = new AgentResponseDto();
    agentDto.setAgentId(agentId); 

    User user = existingAgent.getUser();
    boolean userUpdated = false;
    
    if (agentRequestDto.getUsername() != null) {
        user.setUsername(agentRequestDto.getUsername());
        userUpdated = true;
    }
    if (agentRequestDto.getEmail() != null) {
        user.setEmail(agentRequestDto.getEmail());
        userUpdated = true;
    }

    if (userUpdated) {
        userRepository.save(user);

        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(user.getId());
        if (agentRequestDto.getUsername() != null) {
            userDto.setUsername(agentRequestDto.getUsername());
        }
        if (agentRequestDto.getEmail() != null) {
            userDto.setEmail(agentRequestDto.getEmail());
        }
        agentDto.setUserResponseDto(userDto);
    }

    if (agentRequestDto.getCity_id() != null) {
        City city = cityRepository.findById(agentRequestDto.getCity_id())
                .orElseThrow(() -> new RuntimeException("City not found with id: " + agentRequestDto.getCity_id()));
        existingAgent.setCity(city);
        CityResponseDto cityDto = convertCityToCityResponseDto(city);
        agentDto.setCity(cityDto);
    }

    if (agentRequestDto.getName() != null) {
        existingAgent.setName(agentRequestDto.getName());
        agentDto.setName(agentRequestDto.getName());
    }
    if (agentRequestDto.getPhoneNumber() != null) {
        existingAgent.setPhoneNumber(agentRequestDto.getPhoneNumber());
        agentDto.setPhoneNumber(agentRequestDto.getPhoneNumber());
    }
    
    if (agentRequestDto.isActive() != existingAgent.isActive()) {
        existingAgent.setActive(agentRequestDto.isActive());
        agentDto.setActive(agentRequestDto.isActive());
    }

    agentRepository.save(existingAgent);

 

return agentDto;
}


@Override
public EmployeeResponseDto updateEmployeeById(long employeeId, EmployeeRequestDto employeeRequestDto) {
	Employee existingEmployee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

    boolean userUpdated = false;
    boolean employeeUpdated = false;

    EmployeeResponseDto employeeDto = new EmployeeResponseDto();
    employeeDto.setEmployeeId(employeeId); 

    User user = existingEmployee.getUser();
    
    if (employeeRequestDto.getUsername() != null) {
        user.setUsername(employeeRequestDto.getUsername());
        userUpdated = true;
    }
    if (employeeRequestDto.getEmail() != null) {
        user.setEmail(employeeRequestDto.getEmail());
        userUpdated = true;
    }

    if (userUpdated) {
        userRepository.save(user);

        employeeDto.setUserId(user.getId());
        if (employeeRequestDto.getUsername() != null) {
            employeeDto.setUsername(employeeRequestDto.getUsername());
        }
        if (employeeRequestDto.getEmail() != null) {
            employeeDto.setEmail(employeeRequestDto.getEmail());
        }
    }

    if (employeeRequestDto.getName() != null) {
        existingEmployee.setName(employeeRequestDto.getName());
        employeeUpdated = true;
        employeeDto.setName(employeeRequestDto.getName());
    }
    
    if (employeeRequestDto.isActive() != existingEmployee.isActive()) {
        existingEmployee.setActive(employeeRequestDto.isActive());
        employeeDto.setActive(employeeRequestDto.isActive());
        employeeUpdated = true;
    }

    if (employeeUpdated || userUpdated) {
        System.out.println("Saving employee: " + existingEmployee);  
        employeeRepository.save(existingEmployee);
        System.out.println("Employee saved: " + existingEmployee);  
    }

    return employeeDto;
}

@Override 
public String createState(StateRequest stateRequest) { 
    State state = new State(); 
    state.setName(stateRequest.getName()); 
    state.setIsActive(true); 
    stateRepository.save(state); 
    return "State Added Successfully"; 
} 
 
@Override 
public PagedResponse<StateResponse> getAllStates(int page, int size, String sortBy, String direction) { 
    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending(); 
    PageRequest pageable = PageRequest.of(page, size, sort); 
    Page<State> statePage = stateRepository.findAll(pageable); 
     
    List<StateResponse> stateResponseList = statePage.getContent().stream().map(state -> { 
        StateResponse response = new StateResponse(); 
        response.setStateId(state.getStateId()); 
        response.setName(state.getName()); 
        response.setActive(state.getIsActive()); 
        response.setCities(state.getCities().stream().map(city -> { 
            CityResponse cityResponse = new CityResponse(); 
            cityResponse.setCityId(city.getId()); 
            cityResponse.setName(city.getCity_name()); 
            cityResponse.setActive(city.getIsActive()); 
            return cityResponse; 
        }).collect(Collectors.toList())); 
        return response; 
    }).collect(Collectors.toList()); 
     
    return new PagedResponse<>(stateResponseList, statePage.getNumber(), statePage.getSize(), statePage.getTotalElements(), statePage.getTotalPages(), statePage.isLast()); 
} 
@Override 
public String deactivateStateById(long id) { 
 State state = stateRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("State not found")); 
    if (state.getIsActive()) { 
        state.setIsActive(false); 
        stateRepository.save(state); 
    } else { 
        throw new IllegalStateException("State is already deactivated"); 
    } 
    return "State deactivated successfully"; 
} 
 
@Override 
public String activateStateById(long id) { 
  
     
    State state = stateRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("State not found")); 
    if (!state.getIsActive()) { 
        state.setIsActive(true); 
        stateRepository.save(state); 
    } else { 
        throw new IllegalStateException("State is already activated"); 
    } 
    return "State activated successfully"; 
 
 
} 
 
@Override 
public String createCity(CityRequest cityRequest) { 
  try { 
         State state = stateRepository.findById(cityRequest.getState_id()) 
                 .orElseThrow(() -> new IllegalArgumentException("Invalid state ID")); 
          
         City city = new City(); 
         city.setCity_name(cityRequest.getName()); 
         city.setState(state); 
         city.setIsActive(true); 
         cityRepository.save(city); 
         return "City Added Successfully"; 
     } catch (Exception e) { 
         throw new RuntimeException("Error creating city: " + e.getMessage()); 
     } 
} 
 
@Override 
public String deactivateCity(long id) { 
 City city = cityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("City not found")); 
    if (city.getIsActive()) { 
        city.setIsActive(false); 
        cityRepository.save(city); 
    } 
    return "City deactivated successfully"; 
} 
 
@Override 
public CityResponse getCityById(long id) { 
  City city = cityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("City not found")); 
     CityResponse cityResponse = new CityResponse(); 
     cityResponse.setCityId(city.getId()); 
     cityResponse.setName(city.getCity_name()); 
     cityResponse.setActive(city.getIsActive()); 
     return cityResponse; 
} 
 
@Override 
public String activateCity(long id) { 
  City city = cityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("City not found")); 
     if (!city.getIsActive()) { 
         city.setIsActive(true); 
         cityRepository.save(city); 
     } 
     return "City activated successfully"; 
} 
 


@Override 
public PagedResponse<CityResponse> getAllCities(int page, int size, String sortBy, String direction) { 
    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending(); 
       PageRequest pageable = PageRequest.of(page, size, sort); 
       Page<City> cityPage = cityRepository.findAll(pageable); 
        
       List<CityResponse> cityResponseList = cityPage.getContent().stream().map(city -> { 
           CityResponse response = new CityResponse(); 
           response.setCityId(city.getId()); 
           response.setName(city.getCity_name()); 
           response.setActive(city.getIsActive()); 
           return response; 
       }).collect(Collectors.toList()); 
        
       return new PagedResponse<>(cityResponseList, cityPage.getNumber(), cityPage.getSize(), cityPage.getTotalElements(), cityPage.getTotalPages(), cityPage.isLast()); 
   }


}
