package org.sid.projetcv.controller;

import org.sid.projetcv.dto.AdminDTO;
import org.sid.projetcv.entity.Admin;
import org.sid.projetcv.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@PostMapping("/add")
	public ResponseEntity<Admin> addadmin(@RequestBody AdminDTO admindto){
		return ResponseEntity.ok(adminService.addadmin(admindto));
	}

}
