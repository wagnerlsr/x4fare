package br.com.x4fare.services.impl;

import br.com.x4fare.dtos.BusDto;
import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.models.Bus;
import br.com.x4fare.repositories.BusRepository;
import br.com.x4fare.services.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

	private final BusRepository busRepository;

	@Override
	public List<Bus> buses() {
		return busRepository.findAll();
	}

	@Override
	public ResponseDto insertBus(BusDto bus) {
		try {
			if (busRepository.findByName(bus.getName()).size() == 0)
				busRepository.save(Bus.builder()
						.name(bus.getName())
						.tripFare(bus.getTripFare())
						.build());
			else
				return ResponseDto.builder().success(false).messages(Collections.singletonList("Linha já cadastrada")).build();

			return ResponseDto.builder().success(true).messages(Collections.singletonList("Linha criada com sucesso")).build();
		} catch (Exception e) {
			return ResponseDto.builder().success(false).messages(Collections.singletonList(e.getMessage())).build();
		}
	}

}
