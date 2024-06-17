package sg.nus.iss.java.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.java.model.Employee;
import sg.nus.iss.java.model.User;

public interface UserRepository extends JpaRepository <User, Integer>{

	@Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE CONCAT('%',LOWER(:k),'%')")
	List<User> findUserByName(@Param("k") String keyword);

	@Query("SELECT u FROM User u WHERE u.username = :username")
	Optional<User> findUserByUsername(@Param("username") String username);

	@Query("SELECT u FROM User u WHERE u.type = :type")
	List<Employee> findAllUsersByType(@Param("type") String type);
}
