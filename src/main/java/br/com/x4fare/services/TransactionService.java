package br.com.x4fare.services;

import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.dtos.TransactionDto;
import br.com.x4fare.dtos.UserDto;
import br.com.x4fare.models.User;

import java.util.List;


public interface TransactionService {

	ResponseDto transaction(TransactionDto transaction);

}
