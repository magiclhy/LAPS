package sg.nus.iss.java.service;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sg.nus.iss.java.model.CompensationClaim;
import sg.nus.iss.java.model.Leave;
import sg.nus.iss.java.repository.LeaveRepository;

@Service
public class ExportCSV {
	private static final Logger log = LoggerFactory.getLogger(ExportCSV.class);

    private final LeaveRepository leaveRepository;

    public ExportCSV(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    public void writeLeavesToCsv(Writer writer, List<Leave> leaves, LocalDate startDateSelected, LocalDate endDateSelected) {
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
        	csvPrinter.printRecord("Selected Period:", "from", startDateSelected, "to", endDateSelected);
            csvPrinter.printRecord("Name", "Start Date", "End Date", "Duration (days)", "Leave Type");
            for (Leave leave : leaves) {
                csvPrinter.printRecord(leave.getEmployee().getName(), leave.getStartDate(), leave.getEndDate(), leave.getDuration(), leave.getType());
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }
    
    public void writeClaimsToCsv(Writer writer, List<CompensationClaim> claims) {
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("Name", "Date", "Start Time", "End Time", "Hours", "Remarks");
            for (CompensationClaim claim : claims) {
                csvPrinter.printRecord(claim.getEmployee().getName(), claim.getDate(), claim.getStartTime(), claim.getEndTime(), claim.getHours(), claim.getRemarks());
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }
}
