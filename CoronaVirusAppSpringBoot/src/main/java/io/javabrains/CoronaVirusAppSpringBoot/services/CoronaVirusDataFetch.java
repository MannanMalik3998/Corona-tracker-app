package io.javabrains.CoronaVirusAppSpringBoot.services;

import io.javabrains.CoronaVirusAppSpringBoot.models.locationStatistics;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

// Class behaves as a service
@Service
public class CoronaVirusDataFetch {

    // List to store location wise data
    private List<locationStatistics> allStatistics= new ArrayList<>();

    public List<locationStatistics> getAllStatistics() {
        return allStatistics;
    }

    // Link that contains raw csv data
    private static String urlData="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    @PostConstruct//Ensures that this service be called once the app initializes
    @Scheduled(cron = "* * 1 * * *")//Adding a scheduler to run the program after a particular interval
    /*
    *      "* * * * * *" represents seconds, minutes, hours, day, months, Day of the week
    * */

    public void FetchData() throws IOException, InterruptedException {

        //Daily stats
        List<locationStatistics> newStatistics= new ArrayList<>();

        // **********Obtaining the data from URL**************
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlData))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        // ****************************************************
        //System.out.println(httpResponse.body());//Displays data

        StringReader csvBodyReader = new StringReader(httpResponse.body());
         //using CSV library to read csv
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            locationStatistics locationStat = new locationStatistics();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));

            locationStat.setTotalCases(latestCases);
            System.out.println(locationStat);
            newStatistics.add(locationStat);

        }

        this.allStatistics = newStatistics;
    }
}
