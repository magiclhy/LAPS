package sg.nus.iss.java.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import sg.nus.iss.java.model.Leave;
import sg.nus.iss.java.repository.EmployeeRepository;
import sg.nus.iss.java.repository.LeaveRepository;

@Service
@Transactional(readOnly=true)
public class LeaveServiceImpl implements LeaveService{
	@Resource
	private LeaveRepository leaveRepository;
	
	@Resource
	private EmployeeRepository employeeRepository;
	
	@Override
	public Optional<Leave> findLeave(int id){
		return leaveRepository.findById(id);
	}
	
	@Transactional (readOnly=false)
	@Override
	public Leave createLeave(Leave leave) {
		return leaveRepository.save(leave);
	}

	@Transactional(readOnly=false)
	@Override
	public void deleteLeave(int id) {
		leaveRepository.deleteById(id);
	}

	@Transactional(readOnly=false)
	@Override
	public Leave saveLeave(Leave leave) {
		return leaveRepository.save(leave);
	}

	@Override
	public List<Leave> findEmpLeavesForApproval(int id) {
		return leaveRepository.findEmpLeavesforApproval(id);
	}
	
	@Override
	public List<Leave> findManLeavesForApproval(int id) {
		return leaveRepository.findManLeavesforApproval(id);
	}

	@Override
	public List<Leave> findAllLeavesDuringLeavePeriod(int id, LocalDate startDate, LocalDate endDate) {
		return leaveRepository.findAllLeavesDuringLeavePeriod(id, startDate, endDate);
	}

	@Override
	public Page<Leave> findAllLeavesOfEmpSub(int id, int pageNo, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
		Page<Leave> leavePage = leaveRepository.findAllLeavesOfEmpSub(id, pageRequest);
		return leavePage;
	}
	
	@Override
	public Page<Leave> findAllLeavesOfManSub(int id, int pageNo, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
		Page<Leave> leavePage = leaveRepository.findAllLeavesOfManSub(id, pageRequest);
		return leavePage;
	}

	@Override
	public List<Leave> findAllLeavesCurrMonth() {
		return leaveRepository.findAllLeavesCurrMonth();
	}

	@Override
	public List<Leave> findAllLeavesPrevMonth() {
		return leaveRepository.findAllLeavesPrevMonth();
	}

	@Override
	public List<Leave> findAllLeavesNextMonth() {
		return leaveRepository.findAllLeavesNextMonth();
	}

	@Override
	public Page<Leave> findAllLeavesByPage(int id, int pageNo, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
		Page<Leave> leavePage = leaveRepository.findAllByUserIdInPages(id, pageRequest);
		return leavePage;
	}
}
