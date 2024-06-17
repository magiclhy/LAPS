package sg.nus.iss.java.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import sg.nus.iss.java.model.Leave;

public interface LeaveService {
	Optional<Leave> findLeave(int id);
	Leave createLeave(Leave leave);
	void deleteLeave(int id);
	Leave saveLeave(Leave leave);
	List<Leave> findLeavesForApproval(int id);
	List<Leave> findAllLeavesDuringLeavePeriod(int id, LocalDate startDate, LocalDate endDate);
	Page<Leave> findAllLeavesOfSub(int id, int pageNo, int pageSize);
	List<Leave> findAllLeavesCurrMonth();
	List<Leave> findAllLeavesPrevMonth();
	List<Leave> findAllLeavesNextMonth();
	Page<Leave> findAllLeavesByPage(int id, int pageNo, int pageSize);
}
