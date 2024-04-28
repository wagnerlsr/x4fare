package br.com.x4fare.controllers;

import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.dtos.TransactionDto;
import br.com.x4fare.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@RequiredArgsConstructor
public class TransactionController {
	private final TransactionService transactionService;


	@PostMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDto> transaction(@Valid @RequestBody TransactionDto transaction) {
		ResponseDto response = transactionService.transaction(transaction);

		if (response.getSuccess())
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

}
