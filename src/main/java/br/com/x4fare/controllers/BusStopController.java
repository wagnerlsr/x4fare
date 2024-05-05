package br.com.x4fare.controllers;

import br.com.x4fare.services.BusStopService;
import br.com.x4fare.services.webclient.BusStopClient;
import feign.FeignException;
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
	private final BusStopClient busStopClient;


	@GetMapping(value = "/bus/stop_timetables/{atcocode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> stopTimetables(@PathVariable("atcocode") String atcocode) {
		return ResponseEntity.status(HttpStatus.OK).body(busStopService.stopTimetables(atcocode));
	}

	@GetMapping("/bus/stop_timetables/feign/{atcocode}")
	public ResponseEntity<Object> stopTimetablesFeign(@PathVariable("atcocode") String atcocode) {
		try {
			var res = busStopClient.getStopTimetables(atcocode,
					"ee9dddff", "6b6193e54d358084bd4fd58ecb9ed1b7");

			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (FeignException fe) {
			return ResponseEntity.status(HttpStatus.OK).body("FE: "+fe.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body("E: "+e.getMessage());
		}
	}

}
