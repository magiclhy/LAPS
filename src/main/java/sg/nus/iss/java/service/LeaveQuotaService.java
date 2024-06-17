package sg.nus.iss.java.service;

import java.util.List;
import java.util.Optional;

import sg.nus.iss.java.model.LeaveQuota;

public interface LeaveQuotaService {

	LeaveQuota findLeaveQuota(String currentYear, String role);

	List<LeaveQuota> findAllLeaveQuotas();

	LeaveQuota saveLeaveQuota(LeaveQuota leaveQuota);

	Optional<LeaveQuota> findLeaveQuota(int id);
	
	void deleteLeaveQuota(LeaveQuota leaveQuota);

}
