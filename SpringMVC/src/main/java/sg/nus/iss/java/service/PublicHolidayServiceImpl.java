package sg.nus.iss.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import sg.nus.iss.java.model.PublicHoliday;
import sg.nus.iss.java.repository.PublicHolidayRepository;

@Service
@Transactional(readOnly=true)
public class PublicHolidayServiceImpl implements PublicHolidayService{

	@Resource
	private PublicHolidayRepository publicHolidayRepository;
	
	@Override
	public List<PublicHoliday> findAllPublicHolidays() {
		return publicHolidayRepository.findAll();
	}

	@Override
	@Transactional(readOnly=false)
	public PublicHoliday savePH(PublicHoliday publicHoliday) {
		return publicHolidayRepository.save(publicHoliday);
	}

	@Override
	public Optional<PublicHoliday> findPH(int id) {
		return publicHolidayRepository.findById(id);
	}

	@Override
	@Transactional(readOnly=false)
	public void deletePH(PublicHoliday publicHoliday) {
		publicHolidayRepository.delete(publicHoliday);		
	}

}
