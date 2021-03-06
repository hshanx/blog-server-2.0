package fun.baozi.boot.uc;

import fun.baozi.boot.core.constants.AppConstants;
import fun.baozi.boot.core.exception.AppException;
import fun.baozi.boot.dao.service.ArticleService;
import fun.baozi.boot.domain.Article;
import fun.baozi.boot.domain.Tag;
import fun.baozi.boot.domain.common.ErrorCode;
import fun.baozi.boot.helper.SplitterHelper;
import fun.baozi.boot.helper.StringHelper;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import fun.baozi.boot.dao.service.TagService;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author baozi
 * 2019-07-14
 */
@Component
public class TagManageUc implements TagManage {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagManageUc.class);

    @Autowired
    private TagService tagService;
    @Autowired
    private SplitterHelper splitterHelper;
    @Autowired
    private StringHelper stringHelper;
    @Autowired
    private ArticleService articleService;

    @Override
    public List<Tag> listAll() {
        return tagService.listAll();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteTag(Long id) {
        Tag tag = tagService.findById(id);
        if (tag == null) {
            throw new AppException(ErrorCode.APP_ERROR_PARAM_ILLEGAL, "标签不存在,删除失败");
        }
        try {
            // 先删除
            tagService.deleteById(id);
            // 更新文章的标签
            updateArticleTags(id, tag);
        } catch (Exception e) {
            LOGGER.error("删除标签失败, tagId={}|error={}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateTag(Tag updateTag) {
        Tag tag = tagService.findById(updateTag.getId());
        if (tag == null) {
            throw new AppException(ErrorCode.APP_ERROR_PARAM_ILLEGAL, "标签不存在,更新失败");
        }
        tag.setTagName(updateTag.getTagName());
        tag.setArticleIds(updateTag.getArticleIds());
        tagService.updateByPrimaryKeySelective(tag);
    }

    /**
     * 更新文章的标签
     * @param id 标签id
     * @param tag 标签
     */
    private void updateArticleTags(Long id, Tag tag) {
        String articleIds = tag.getArticleIds();
        List<String> idList = splitterHelper.splitToStringList(articleIds, AppConstants.COMMON_SPLIT);
        List<Article> articleList = articleService.listByArticleIdList(idList);
        for (Article article : articleList) {
            String tags = article.getTags();
            if (Strings.isNotEmpty(tags)) {
                tags = stringHelper.wipeSpecifyStr(tags, String.valueOf(id));
                article.setTags(tags);
            }
        }
        articleService.batchUpdate(articleList);
    }
}
