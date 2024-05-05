	package br.com.x4fare.services.impl;

import br.com.x4fare.dtos.BusDto;
import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.dtos.UserDto;
import br.com.x4fare.models.Bus;
import br.com.x4fare.models.User;
import br.com.x4fare.repositories.BusRepository;
import br.com.x4fare.repositories.UserRepository;
import br.com.x4fare.services.BusService;
import br.com.x4fare.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public List<User> users() {
		return userRepository.findAll();
	}

	@Override
	public ResponseDto insertUser(UserDto user) {
		try {
			if (userRepository.findByName(user.getName()).size() == 0)
				userRepository.save(User.builder()
						.name(user.getName())
						.type(user.getType().toString())
						.build());
			else
				return ResponseDto.builder().success(false).messages(Collections.singletonList("Usuário já cadastrado")).build();

			return ResponseDto.builder().success(true).messages(Collections.singletonList("Usuário criado com sucesso")).build();
		} catch (Exception e) {
			return ResponseDto.builder().success(false).messages(Collections.singletonList(e.getMessage())).build();
		}
	}

}
