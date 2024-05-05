package br.com.x4fare.services.webclient;

import br.com.x4fare.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(configuration = FeignConfig.class, value = "BusStop", url = "https://transportapi.com/v3/uk/bus/stop_timetables")
public interface BusStopClient {

    @GetMapping("/{atcocode}")
    Object getStopTimetables(@PathVariable("atcocode") String atcocode,
                             @RequestParam("app_id") String appId,
                             @RequestParam("app_key") String appKey);

}
