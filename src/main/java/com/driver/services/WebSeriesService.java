package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

        if(webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName()) != null){
            throw new Exception("Series is already present");
        }

        WebSeries webSeries = new WebSeries();
        webSeries.setSeriesName(webSeries.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());

        ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();
        productionHouse.getWebSeriesList().add(webSeries);
        webSeries.setProductionHouse(productionHouse);

        WebSeries savedWebSeries = webSeriesRepository.save(webSeries);

        double averageOfRatings;
        List<WebSeries> webSeriesList = productionHouse.getWebSeriesList();
        double sumOfRatings = 0;
        for(WebSeries webSeries1 : webSeriesList){
            sumOfRatings += webSeries1.getRating();
        }

        averageOfRatings = sumOfRatings / webSeriesList.size();
        productionHouse.setRatings(averageOfRatings);

        ProductionHouse savedProductionHouse = productionHouseRepository.save(productionHouse);
        return savedWebSeries.getId();
    }

}
