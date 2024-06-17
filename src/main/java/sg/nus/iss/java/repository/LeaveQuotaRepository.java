package sg.nus.iss.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.nus.iss.java.model.LeaveQuota;

@Repository
public interface LeaveQuotaRepository extends JpaRepository <LeaveQuota, Integer>{
	
	@Query("SELECT lq FROM LeaveQuota lq WHERE lq.year = :currentYear AND lq.role = :role")
	LeaveQuota findLeaveQuota(@Param("currentYear") String currentYear, @Param("role") String role);

}
