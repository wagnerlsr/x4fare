package br.com.x4fare.services.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;


@Slf4j
@Service
@RequiredArgsConstructor
public class HttpService {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String HOST = "https://transportapi.com/v3/uk/bus/stop_timetables";
    private static final String APP_ID = "ee9dddff";
    private static final String APP_KEY = "6b6193e54d358084bd4fd58ecb9ed1b7";


    private static String[] getHeaders() {
        return Arrays.asList(
                "Accept", "*/*",
                "Accept-Charset", "UTF-8"
//                "Accept-Encoding", "gzip, deflate, br"
        ).toArray(new String[0]);
    }

    public String getStopTimetables(String ATCOCode) {
        try {
            var url = String.format("%s/%s?app_id=%s&app_key=%s", HOST, ATCOCode, APP_ID, APP_KEY);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .headers(getHeaders())
                    .GET()
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200)
                return response.body();
        } catch (Exception e) {
            log.info(String.format("ERROR getStopTimetables [ %s ]", e.getMessage()));
        }

        return null;
    }

}
