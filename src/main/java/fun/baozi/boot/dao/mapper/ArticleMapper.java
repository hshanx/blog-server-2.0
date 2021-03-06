package fun.baozi.boot.dao.mapper;

import fun.baozi.boot.dao.TagSearchParam;
import fun.baozi.boot.domain.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import fun.baozi.boot.web.req.BlogSearchForm;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ArticleMapper extends Mapper<Article> {

    List<Article> selectByCondition(BlogSearchForm searchModel);

    Long countArticles(BlogSearchForm searchModel);

    List<Article> listByIds(@Param("idList")List<Long> idList);

    List<Article> listByIdListWithPage(TagSearchParam param);

    Long countByIdList(TagSearchParam param);

    List<Article> searchByText(@Param("text") String text);

    Integer countByText(@Param("text") String text);

    List<Article> listByArticleIdList(@Param("articleIdList")List<String> articleIdList);

    int batchUpdate(@Param("articleList") List<Article> articleList);

    int batchInsert(@Param("articleList") List<Article> articleList);

    /**
     * 增加访问量
     * @param articleId 文章主键id
     */
    void increaseVisit(@Param("articleId") Long articleId);

    /**
     * 置顶文章
     * @param articleId 文章id
     */
    void sortArticle(@Param("sort") Byte sort, @Param("articleId") String articleId);
}