package br.com.x4fare.controllers;

import br.com.x4fare.services.ReportService;
import br.com.x4fare.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@Validated
@RestController
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;


	@GetMapping(value = "/reports/daily-report", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> dailyReport(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
											  @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate) {
		return ResponseEntity.status(HttpStatus.OK).body(reportService.dailyReport(startDate, endDate));
	}

}
