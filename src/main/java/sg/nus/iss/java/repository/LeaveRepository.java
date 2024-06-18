package sg.nus.iss.java.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.java.model.Leave;

public interface LeaveRepository extends JpaRepository <Leave, Integer>{
	@Query("SELECT l FROM Leave l WHERE l.employee.id = :id "
			+ "AND YEAR(startDate) = YEAR(CURDATE()) ORDER BY startDate")
	Page<Leave> findAllByUserIdInPages(@Param("id") int id, Pageable pageable);

	@Query("SELECT l FROM Leave l " +
			"JOIN l.employee e " + 
			"JOIN e.manager m WHERE m.id = :id "
			+ "AND (l.status = 'Applied' OR l.status = 'Updated') "
			+ "ORDER BY e.name")
	List<Leave> findLeavesforApproval(@Param("id") int id);
	
	@Query("SELECT l FROM Leave l " +
			"JOIN l.employee e " + 
			"JOIN e.manager m WHERE m.id = :id "
			+ "AND l.status = 'Approved' "
			+ "AND (l.startDate BETWEEN :startDate AND :endDate OR "
			+ "l.endDate BETWEEN :startDate AND :endDate) "
			+ "ORDER BY l.startDate")
	List<Leave> findAllLeavesDuringLeavePeriod(@Param("id") int id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query("SELECT l FROM Leave l " +
			"JOIN l.employee e " + 
			"JOIN e.manager m WHERE m.id = :id "
			+ "AND (l.status = 'Approved' OR l.status = 'Rejected') "
			+ "AND YEAR(startDate) = YEAR(CURDATE()) "
			+ "ORDER BY l.startDate")
	Page<Leave> findAllLeavesOfSub(@Param("id") int id, Pageable pageable);

	@Query("SELECT l FROM Leave l "
			+ "WHERE l.status = 'Approved' "
			+ "AND (MONTH(startDate) = MONTH(CURDATE())) "
			+ "AND YEAR(startDate) = YEAR(CURDATE()) "
			+ "ORDER BY l.startDate")
	List<Leave> findAllLeavesCurrMonth();
	
	@Query("SELECT l FROM Leave l "
			+ "WHERE l.status = 'Approved' "
			+ "AND (MONTH(startDate) = MONTH(CURDATE()) + 1) "
			+ "AND YEAR(startDate) = YEAR(CURDATE()) "
			+ "ORDER BY l.startDate")
	List<Leave> findAllLeavesPrevMonth();
	
	@Query("SELECT l FROM Leave l "
			+ "WHERE l.status = 'Approved' "
			+ "AND (MONTH(startDate) = MONTH(CURDATE()) - 1) "
			+ "AND YEAR(startDate) = YEAR(CURDATE()) "
			+ "ORDER BY l.startDate")
	List<Leave> findAllLeavesNextMonth();
}
