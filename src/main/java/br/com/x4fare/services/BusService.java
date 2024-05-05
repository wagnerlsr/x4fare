package br.com.x4fare.services;

import br.com.x4fare.dtos.BusDto;
import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.models.Bus;

import java.util.List;


public interface BusService {

	List<Bus> buses();
	ResponseDto insertBus(BusDto bus);

}
