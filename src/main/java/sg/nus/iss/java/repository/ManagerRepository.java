package sg.nus.iss.java.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.nus.iss.java.model.Manager;

@Repository
public interface ManagerRepository extends JpaRepository <Manager, Integer>{

	@Query("SELECT u FROM User u WHERE u.type = 'Manager'")
	List<Manager> findAllManagers();
	
	@Query("SELECT u FROM User u WHERE u.id = :id")
	Manager findManagerById(@Param("id") Optional<Integer> id);
}
