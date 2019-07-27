package pro.meisen.boot.dao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.meisen.boot.dao.mapper.BasicMapper;
import pro.meisen.boot.dao.mapper.StatisticsMapper;
import pro.meisen.boot.dao.service.StatisticsService;
import pro.meisen.boot.dao.service.basic.BasicServiceImpl;
import pro.meisen.boot.domain.Statistics;


/**
 * @author meisen
 * 2019-07-26
 */
@Service
public class StatisticServiceImpl extends BasicServiceImpl<Statistics> implements StatisticsService {
    @Autowired
    private StatisticsMapper mapper;
    @Override
    public BasicMapper<Long, Statistics> getMapper() {
        return mapper;
    }

    @Override
    public Statistics searchLatest() {
        return mapper.searchLatest();
    }
}