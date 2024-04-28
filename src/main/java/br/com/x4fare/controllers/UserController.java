package br.com.x4fare.controllers;

import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.dtos.UserDto;
import br.com.x4fare.models.User;
import br.com.x4fare.services.BusService;
import br.com.x4fare.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;


	@GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<User>> users() {
		return ResponseEntity.status(HttpStatus.OK).body(userService.users());
	}

	@PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDto> insertUser(@Valid @RequestBody UserDto user) {
		ResponseDto response = userService.insertUser(user);

		if (response.getSuccess())
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

}
