package sg.nus.iss.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.java.model.CompensationClaim;
import sg.nus.iss.java.repository.CompensationClaimRepository;

@Service
@Transactional(readOnly=true)
public class CompensationClaimServiceImpl implements CompensationClaimService{
	@Autowired
	private CompensationClaimRepository compensationClaimRepository;

	@Override
	@Transactional(readOnly=false)
	public CompensationClaim save(CompensationClaim claim) {
		return compensationClaimRepository.save(claim);
	}

	@Override
	public List<CompensationClaim> findEmpClaimsForApproval(int id) {
		return compensationClaimRepository.findEmpClaimsForApproval(id) ;
	}
	
	@Override
	public List<CompensationClaim> findManClaimsForApproval(int id) {
		return compensationClaimRepository.findManClaimsForApproval(id) ;
	}

	@Override
	public Optional<CompensationClaim> findClaim(Integer id) {
		return compensationClaimRepository.findById(id);
	}

	@Override
	public List<CompensationClaim> findAllEmpClaimsByName(int id, String name) {
		return compensationClaimRepository.findAllEmpClaimsByName(id, name);
	}
	
	@Override
	public List<CompensationClaim> findAllManClaimsByName(int id, String name) {
		return compensationClaimRepository.findAllManClaimsByName(id, name);
	}

	@Override
	public List<CompensationClaim> findAllClaimsById(int id) {
		return compensationClaimRepository.findAllClaimsById(id);
	}
}
