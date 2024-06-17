package sg.nus.iss.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.nus.iss.java.model.CompensationClaim;

@Repository
public interface CompensationClaimRepository extends JpaRepository <CompensationClaim, Integer>{

	@Query("SELECT c FROM CompensationClaim c " +
			"JOIN c.employee e " + 
			"JOIN e.manager m WHERE m.id = :id "
			+ "AND c.status = 'Applied' "
			+ "ORDER BY e.name")
	List<CompensationClaim> findClaimsForApproval(@Param("id") int id);

	@Query("SELECT c FROM CompensationClaim c " +
			"JOIN c.employee e " + 
			"JOIN e.manager m WHERE m.id = :id "
			+ "AND c.status = 'Approved' "
			+ "AND LOWER(e.name) like CONCAT('%',LOWER(:name),'%') "
			+ "ORDER BY c.date ")
	List<CompensationClaim> findAllClaimsByName(@Param("id") int id, @Param("name") String name);

	
	@Query("SELECT c FROM CompensationClaim c " +
			"WHERE c.employee.id = :id "
			+ "ORDER BY c.date")
	List<CompensationClaim> findAllClaimsById(@Param("id") int id);

}
