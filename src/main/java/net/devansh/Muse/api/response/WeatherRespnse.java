package net.devansh.Muse.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;


@Getter
@Setter
public class WeatherRespnse{

    private Current current;

    @Getter
    @Setter
    public class Current{
        @JsonProperty("temp_c")
        private double tempC;

        @JsonProperty("is_day")
        private int isDay;

        private int cloud;

        @JsonProperty("feelslike_c")
        private double feelslikeC;

    }


}

