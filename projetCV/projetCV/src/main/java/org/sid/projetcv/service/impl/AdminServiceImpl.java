package org.sid.projetcv.service.impl;

import org.sid.projetcv.dto.AdminDTO;
import org.sid.projetcv.entity.Admin;
import org.sid.projetcv.repository.AdminRepository;
import org.sid.projetcv.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Override
	public Admin addadmin(AdminDTO admindto) {
		Admin admin = new Admin();
		
		admin.setFirstName(admindto.getFirstName());
		admin.setLastName(admindto.getLastName());
		admin.setEmail(admindto.getEmail());
		admin.setPassword(admindto.getPassword());
		
		return adminRepository.save(admin);
	}

}
