package br.com.x4fare.services.impl;

import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.dtos.TransactionDto;
import br.com.x4fare.models.Bus;
import br.com.x4fare.models.Transaction;
import br.com.x4fare.models.User;
import br.com.x4fare.repositories.BusRepository;
import br.com.x4fare.repositories.TransactionRepository;
import br.com.x4fare.repositories.UserRepository;
import br.com.x4fare.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BusRepository busRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseDto transaction(TransactionDto transaction) {
        try {
            Bus bus = busRepository.findById(transaction.getBusId()).orElse(null);
            User user = userRepository.findById(transaction.getUserId()).orElse(null);

            if (bus != null) {
                if (user != null) {
                    transactionRepository.save(Transaction.builder()
                            .bus(bus)
                            .user(user)
                            .transactionDate(transaction.getTransactionDate())
                            .build());

                    return ResponseDto.builder().success(true).messages(Collections.singletonList("Transação criada com sucesso")).build();
                } else {
                    return ResponseDto.builder().success(false).messages(Collections.singletonList("Usuário invalido")).build();
                }
            } else {
                return ResponseDto.builder().success(false).messages(Collections.singletonList("Linha invalida")).build();
            }
        } catch (Exception e) {
            return ResponseDto.builder().success(false).messages(Collections.singletonList(e.getMessage())).build();
        }
    }

}
