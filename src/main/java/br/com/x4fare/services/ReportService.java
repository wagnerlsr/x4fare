package br.com.x4fare.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface ReportService {

	HashMap<String, List<HashMap<String, Object>>> dailyReport(LocalDate startDate, LocalDate endDate);

}
