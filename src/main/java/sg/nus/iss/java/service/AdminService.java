package sg.nus.iss.java.service;

import java.util.Optional;

import sg.nus.iss.java.model.Admin;

public interface AdminService {
	Admin saveAdmin(Admin admin);

	Optional<Admin> findAdminById(int id);
}
