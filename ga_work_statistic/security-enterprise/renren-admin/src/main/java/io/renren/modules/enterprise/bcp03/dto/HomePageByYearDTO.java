package io.renren.modules.enterprise.bcp03.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomePageByYearDTO {

    private List<String> xAxis;

    private List<String> series;
}
