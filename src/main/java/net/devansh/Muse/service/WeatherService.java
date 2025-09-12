package net.devansh.Muse.service;

import net.devansh.Muse.api.response.WeatherRespnse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    @Value("${weather.api}")
    private  String apiKey ;

    private static final String API = "http://api.weatherapi.com/v1/current.json?key=API_KEY&q=CITY";

    @Autowired
    private RestTemplate restTemplate;

    public WeatherRespnse getWeather(String city){
        String finalAPI = API.replace("API_KEY",apiKey).replace("CITY",city);
        ResponseEntity<WeatherRespnse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherRespnse.class);
        WeatherRespnse body = response.getBody();
        return body;
    }



}
