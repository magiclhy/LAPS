package sg.nus.iss.java.service;

import java.util.List;
import java.util.Optional;

import sg.nus.iss.java.model.CompensationClaim;

public interface CompensationClaimService {

	CompensationClaim save(CompensationClaim claim);

	List<CompensationClaim> findEmpClaimsForApproval(int id);
	
	List<CompensationClaim> findManClaimsForApproval(int id);

	Optional<CompensationClaim> findClaim(Integer id);

	List<CompensationClaim> findAllEmpClaimsByName(int id, String name);
	
	List<CompensationClaim> findAllManClaimsByName(int id, String name);

	List<CompensationClaim> findAllClaimsById(int id);

}
