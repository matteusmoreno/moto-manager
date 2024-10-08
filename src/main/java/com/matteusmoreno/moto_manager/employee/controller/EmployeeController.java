package com.matteusmoreno.moto_manager.employee.controller;

import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.request.CreateEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.request.UpdateEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.response.EmployeeDetailsResponse;
import com.matteusmoreno.moto_manager.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/create")
    public ResponseEntity<EmployeeDetailsResponse> create(@RequestBody @Valid CreateEmployeeRequest request, UriComponentsBuilder uriBuilder) {
        Employee employee = employeeService.createEmployee(request);
        URI uri = uriBuilder.path("/employees/create/{id}").buildAndExpand(employee.getId()).toUri();

        return ResponseEntity.created(uri).body(new EmployeeDetailsResponse(employee));
    }

    @GetMapping("/find-all")
    public ResponseEntity<Page<EmployeeDetailsResponse>> findAllEmployees(@PageableDefault(sort = "name", size = 10) Pageable pageable) {
        Page<EmployeeDetailsResponse> page = employeeService.findAllEmployees(pageable);

        return ResponseEntity.ok(page);
    }

    @PutMapping("/update")
    public ResponseEntity<EmployeeDetailsResponse> update(@RequestBody @Valid UpdateEmployeeRequest request) {
        Employee employee = employeeService.updateEmployee(request);

        return ResponseEntity.ok(new EmployeeDetailsResponse(employee));
    }

    @DeleteMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable UUID id) {
        employeeService.disableEmployee(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/enable/{id}")
    public ResponseEntity<EmployeeDetailsResponse> enable(@PathVariable UUID id) {
        Employee employee = employeeService.enableEmployee(id);

        return ResponseEntity.ok(new EmployeeDetailsResponse(employee));
    }
}
