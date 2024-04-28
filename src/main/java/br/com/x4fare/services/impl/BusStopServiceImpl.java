package br.com.x4fare.services.impl;

import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.services.BusStopService;
import br.com.x4fare.services.http.HttpService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class BusStopServiceImpl implements BusStopService {

	private final HttpService httpService;

	@Override
	public Object stopTimetables(String atcocode) {
		try {
			var mapper = new ObjectMapper();
			var list = new ArrayList<>();

			var res = httpService.getStopTimetables(atcocode);

			JsonNode jn = mapper.readTree(res);
			var departures = jn.get("departures").get("all");

			departures.forEach(departure -> {
				var map = new HashMap<String, String>();

				map.put("Time", departure.get("aimed_departure_time").asText());
				map.put("Line", departure.get("line").asText());
				map.put("Destination", departure.get("direction").asText());

				list.add(map);
			});

			var map = new HashMap<String, Object>();
			map.put("Departures", list);

			return map;
		} catch (Exception e) {
			return ResponseDto.builder().success(false).messages(Collections.singletonList(e.getMessage())).build();
		}
	}

}
