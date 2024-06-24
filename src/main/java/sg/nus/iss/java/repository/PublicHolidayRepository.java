package sg.nus.iss.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.nus.iss.java.model.PublicHoliday;

public interface PublicHolidayRepository extends JpaRepository <PublicHoliday, Integer>{

}
