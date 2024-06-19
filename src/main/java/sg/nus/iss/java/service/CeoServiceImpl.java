package sg.nus.iss.java.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.java.model.Ceo;
import sg.nus.iss.java.repository.CeoRepository;

@Service
@Transactional(readOnly=true)
public class CeoServiceImpl implements CeoService{
	@Autowired
	private CeoRepository ceoRepository;
	
	@Override
	@Transactional(readOnly=false)
	public Ceo saveCeo(Ceo ceo) {
		return ceoRepository.save(ceo);
	}

	@Override
	public Optional<Ceo> findCeoById(int id) {
		return ceoRepository.findById(id);
	}
}
