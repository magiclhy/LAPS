package sg.nus.iss.java.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.nus.iss.java.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository <Employee, Integer>{

}
