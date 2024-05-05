package br.com.x4fare.controllers;

import br.com.x4fare.dtos.BusDto;
import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.models.Bus;
import br.com.x4fare.services.BusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
public class BusController {

	private final BusService busService;


	@GetMapping(value = "/buses", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Bus>> buses() {
		return ResponseEntity.status(HttpStatus.OK).body(busService.buses());
	}

	@PostMapping(value = "/buses", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDto> insertBus(@Valid @RequestBody BusDto bus) {
		ResponseDto response = busService.insertBus(bus);

		if (response.getSuccess())
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

}
