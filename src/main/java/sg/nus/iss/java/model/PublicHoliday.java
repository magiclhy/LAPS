package sg.nus.iss.java.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="public_holiday")
public class PublicHoliday {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank(message = "Name must not be blank")
	private String name;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate date;
	
	public PublicHoliday() {}
	
	public PublicHoliday(String name, LocalDate date) {
		this.name = name;
		this.date = date;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("DD/MM/YYYY"));
    }
}
