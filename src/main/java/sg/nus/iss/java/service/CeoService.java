package sg.nus.iss.java.service;

import java.util.Optional;

import sg.nus.iss.java.model.Ceo;

public interface CeoService {
	Ceo saveCeo(Ceo ceo);
	
	Optional<Ceo> findCeoById(int id);
}
