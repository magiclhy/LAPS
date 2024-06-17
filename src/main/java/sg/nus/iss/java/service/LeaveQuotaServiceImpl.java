package sg.nus.iss.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.java.model.LeaveQuota;
import sg.nus.iss.java.repository.LeaveQuotaRepository;

@Service
@Transactional(readOnly=true)
public class LeaveQuotaServiceImpl implements LeaveQuotaService{
	
	@Autowired
	private LeaveQuotaRepository leaveQuotaRepository;

	@Override
	public LeaveQuota findLeaveQuota(String currentYear, String role) {
		return leaveQuotaRepository.findLeaveQuota(currentYear, role);
	}

	@Override
	public List<LeaveQuota> findAllLeaveQuotas() {
		return leaveQuotaRepository.findAll();
	}

	@Override
	@Transactional(readOnly=false)
	public LeaveQuota saveLeaveQuota(LeaveQuota leaveQuota) {
		return leaveQuotaRepository.save(leaveQuota);
	}

	@Override
	public Optional<LeaveQuota> findLeaveQuota(int id) {
		return leaveQuotaRepository.findById(id);
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteLeaveQuota(LeaveQuota leaveQuota) {
		leaveQuotaRepository.delete(leaveQuota);	
	}
}
