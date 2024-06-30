package sg.nus.iss.java.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.nus.iss.java.model.LeaveQuota;
import sg.nus.iss.java.service.LeaveQuotaService;

@RestController
@RequestMapping("api/leaveQuota")
public class LeaveQuotaRestController {
	@Autowired
	private LeaveQuotaService leaveQuotaService;
	
	//CREATE LEAVE QUOTA***
	@PostMapping("/create")
	public LeaveQuota createLeaveQuota (@RequestBody LeaveQuota leaveQuota) {
		return leaveQuotaService.saveLeaveQuota(leaveQuota);
	}
	
	//VIEW LEAVE QUOTA***
	@GetMapping("/view")
	public List<LeaveQuota> viewLeaveQuota() {
		return leaveQuotaService.findAllLeaveQuotas();
	}
	
	//VIEW LEAVE QUOTA BY ID ***
	@GetMapping("/view/{id}")
	public ResponseEntity<LeaveQuota> getLeaveQuotaById(@PathVariable int id) {
		Optional<LeaveQuota> optLeaveQuota = leaveQuotaService.findLeaveQuota(id);
		if (optLeaveQuota.isPresent()) {
			LeaveQuota leaveQuota = optLeaveQuota.get();
			return new ResponseEntity<LeaveQuota>(leaveQuota,HttpStatus.OK);
		}
		else {
			return new ResponseEntity<LeaveQuota>(HttpStatus.NOT_FOUND);
		}
	}
	
	//EDIT LEAVE QUOTA***
	@PutMapping("/edit/{id}")
	public ResponseEntity<LeaveQuota> editLeaveQuota (@RequestBody LeaveQuota newLeaveQuota, @PathVariable int id) {
		Optional<LeaveQuota> optLeaveQuota = leaveQuotaService.findLeaveQuota(id);
		if (optLeaveQuota.isPresent()) {
			LeaveQuota leaveQuota = optLeaveQuota.get();
			leaveQuota.setAnnualLeaveQuota(newLeaveQuota.getAnnualLeaveQuota());
			leaveQuota.setMedicalLeaveQuota(newLeaveQuota.getMedicalLeaveQuota());
			leaveQuota.setYear(newLeaveQuota.getYear());
			leaveQuota.setRole(newLeaveQuota.getRole());
			leaveQuota.setEmployees(newLeaveQuota.getEmployees());
			leaveQuotaService.saveLeaveQuota(leaveQuota);
			return new ResponseEntity<LeaveQuota>(leaveQuota,HttpStatus.OK);
		}
		else {
			return new ResponseEntity<LeaveQuota>(HttpStatus.NOT_FOUND);
		}
	}
	
	//DELETE LEAVE QUOTA
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<LeaveQuota> deleteLeaveQuota(@PathVariable int id) {
		Optional<LeaveQuota> optLeaveQuota = leaveQuotaService.findLeaveQuota(id);
		if (optLeaveQuota.isPresent()) {
			LeaveQuota leaveQuota = optLeaveQuota.get();
			leaveQuotaService.deleteLeaveQuota(leaveQuota);
			return new ResponseEntity<LeaveQuota>(HttpStatus.OK);
		}
		else {
			return new ResponseEntity<LeaveQuota>(HttpStatus.NOT_FOUND);
		}
	}
}
