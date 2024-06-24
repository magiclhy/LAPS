package sg.nus.iss.java.model;

import java.time.LocalDate;
import java.time.Year;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import sg.nus.iss.java.model.PublicHoliday;

@Component
public class cyValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PublicHoliday.class.isAssignableFrom(clazz) || LeaveQuota.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        if (obj instanceof PublicHoliday) {
            validatePublicHoliday((PublicHoliday) obj, errors);
        } else if (obj instanceof LeaveQuota) {
            validateLeaveQuota((LeaveQuota) obj, errors);
        }
    }

    Year currentYear = Year.now();
    private void validatePublicHoliday(PublicHoliday publicHoliday, Errors errors) {
        LocalDate holidayDate = publicHoliday.getDate();
        if (holidayDate.getYear() != currentYear.getValue()) {
            errors.rejectValue("date", "error.date", "Enter public holidays for the current year only");
            }
    }
    private void validateLeaveQuota(LeaveQuota leaveQuota, Errors errors) {
        if (!leaveQuota.getYear().equals(String.valueOf(currentYear.getValue()))) {
            errors.rejectValue("year", "error.year", "Enter leave quota for the current year only");
        	}
    }
}
