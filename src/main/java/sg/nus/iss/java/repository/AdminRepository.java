package sg.nus.iss.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.nus.iss.java.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository <Admin, Integer>{

}
