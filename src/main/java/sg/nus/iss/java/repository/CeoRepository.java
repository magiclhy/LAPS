package sg.nus.iss.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.nus.iss.java.model.Ceo;

@Repository
public interface CeoRepository extends JpaRepository <Ceo, Integer>{

}

