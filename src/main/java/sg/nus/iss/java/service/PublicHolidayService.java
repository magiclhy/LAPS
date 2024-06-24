package sg.nus.iss.java.service;

import java.util.List;
import java.util.Optional;

import sg.nus.iss.java.model.PublicHoliday;

public interface PublicHolidayService {
	List<PublicHoliday> findAllPublicHolidays();

	PublicHoliday savePH(PublicHoliday publicHoliday);

	Optional<PublicHoliday> findPH(int id);

	void deletePH(PublicHoliday publicHoliday);
}
