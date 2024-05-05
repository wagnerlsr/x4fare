package br.com.x4fare.services;

import br.com.x4fare.dtos.BusDto;
import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.dtos.UserDto;
import br.com.x4fare.models.Bus;
import br.com.x4fare.models.User;

import java.util.List;


public interface UserService {

	List<User> users();
	ResponseDto insertUser(UserDto user);

}
