package fun.baozi.boot.web.controller;

import fun.baozi.boot.core.exception.AppException;
import fun.baozi.boot.dao.service.ArticleService;
import fun.baozi.boot.dao.service.StatisticsService;
import fun.baozi.boot.domain.Article;
import fun.baozi.boot.domain.Statistics;
import fun.baozi.boot.domain.common.ErrorCode;
import fun.baozi.boot.ext.redis.RedisKey;
import fun.baozi.boot.helper.StringHelper;
import fun.baozi.boot.web.res.StatisticsCount;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author baozi
 * 2019-07-27
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private StringHelper stringHelper;
//    @Autowired
//    private RedisOperation<String> redisOperation;

    @GetMapping("/userVisit")
    public Long userVisit(HttpServletRequest request) {
//        Long count = redisOperation.pdCount(RedisKey.UV.getKey());
//        if (count != null && count != 0L) {
//            return count;
//        }
        Statistics statistics = statisticsService.searchLatest();
        if (null == statistics) {
            return 0L;
        }
        return statistics.getUserVisit();
    }

//    @GetMapping("/popular")
//    public List<Article> popular(HttpServletRequest request, @RequestParam("start") Long start, @RequestParam("end") Long end) {
//        if (null == start || null == end) {
//            return new ArrayList<>();
//        }
//        Set<String> idSet = redisOperation.reverseRange(RedisKey.ARTICLE_INFO.getKey(), start, end);
//        List<Long> idList = new ArrayList<>();
//        // 转换id的类型
//        formatStringToLong(idSet, idList);
//        List<Article> popularArticles = articleService.listByIds(idList);
//        List<Article> resultArticles = new ArrayList<>();
//        // 根据idSet排序
//        sortResultArticles(idList, popularArticles, resultArticles);
//        return resultArticles;
//    }

    @GetMapping("/articleCount")
    public Integer articleCount(HttpServletRequest request) {
        return articleService.selectCount(new Article());
    }

    @GetMapping("/statisticCount")
    public StatisticsCount statisticsCount(HttpServletRequest request) {
        Article condition = new Article();
        Integer articleCount = articleService.selectCount(condition);
        condition.setPublish(true);
        Integer publishCount = articleService.selectCount(condition);
        condition.setPublish(false);
        Integer nonPublishCount = articleService.selectCount(condition);
        StatisticsCount count = new StatisticsCount();
        count.setArticleCount(articleCount);
        count.setPublishCount(publishCount);
        count.setNonPublishCount(nonPublishCount);
        return count;
    }
    /**
     * 将结果排序
     * @param idList 排序的id集合
     * @param popularArticles 未排序的集合
     * @param resultArticles 已排好序的集合
     */
    private void sortResultArticles(List<Long> idList, List<Article> popularArticles, List<Article> resultArticles) {
        for (Long id : idList) {
            for (Article article : popularArticles) {
                if (article.getId().equals(id)) {
                    resultArticles.add(article);
                    break;
                }
            }
        }
    }

    /**
     * 将String类型的Id集合转换为Long类型的id集合
     * @param idSet  string类型
     * @param idList long类型
     */
    private void formatStringToLong(Set<String> idSet, List<Long> idList) {
        for (String id : idSet) {
            if (Strings.isEmpty(id) || !stringHelper.isNumeric(id.trim())) {
                throw new AppException(ErrorCode.APP_ERROR_PARAM_ILLEGAL, "获取失败, 请联系管理员");
            }
            id = id.trim();
            idList.add(Long.valueOf(id));
        }
    }


}
