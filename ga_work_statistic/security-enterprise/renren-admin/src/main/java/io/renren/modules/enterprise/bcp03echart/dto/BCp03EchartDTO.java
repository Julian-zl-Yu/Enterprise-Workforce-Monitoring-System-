package io.renren.modules.enterprise.bcp03echart.dto;

import lombok.Data;

import java.util.List;

@Data
public class BCp03EchartDTO {

    private List<String> yearMonth;

    private List<Long> useController;

    private List<Long> useOrdWorker;

    private List<Long> useTechWorker;

    private List<Long> useSkillWorker;

    private List<Long> lackController;

    private List<Long> lackOrdWorker;

    private List<Long> lackTechWorker;

    private List<Long> lackSkillWorker;
}
