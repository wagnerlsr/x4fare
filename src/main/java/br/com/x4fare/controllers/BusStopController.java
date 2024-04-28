package br.com.x4fare.controllers;

import br.com.x4fare.services.BusStopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@RequiredArgsConstructor
public class BusStopController {

	private final BusStopService busStopService;


	@GetMapping(value = "/bus/stop_timetables/{atcocode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> stopTimetables(@PathVariable("atcocode") String atcocode) {
		return ResponseEntity.status(HttpStatus.OK).body(busStopService.stopTimetables(atcocode));
	}

}
