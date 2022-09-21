package com.xt.framework.db.elasticsearch.core;

import com.xt.framework.db.FrameworkDbApplicationTest;
import com.xt.framework.db.elasticsearch.repositories.model.User;
import com.xt.framework.db.elasticsearch.template.model.Product;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @Description ES
 * @Date 2022/9/20 18:18
 */
class ElasticSearchTemplateTest extends FrameworkDbApplicationTest {
    @Resource
    private ElasticSearchTemplate<Product> productElasticSearchTemplate;
    @Resource
    private ElasticSearchTemplate<User> elasticSearchTemplate;

    @BeforeEach
    public void init() {
        elasticSearchTemplate.setIndexName("user");
        elasticSearchTemplate.setInnerType(User.class);
    }

    @Test
    void saveOrUpdateById() {
        Product product = new Product();
        productElasticSearchTemplate.saveOrUpdateById(product);
        User user = new User();
        elasticSearchTemplate.saveOrUpdateById(user);
    }

    @Test
    void batchSaveOrUpdateById() {
    }

    @Test
    void update() {
    }

    @Test
    void queryList() {
    }

    @Test
    void queryPage() {
    }

    @Test
    void searchOne() {
        BoolQueryBuilder builder=QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("name","test"));
        productElasticSearchTemplate.searchOne(builder);
    }

    @Test
    void aggQuery() {
    }

    @Test
    void scan() {
    }

    @Test
    void scroll() {
    }

    @Test
    void testScan() {
    }

    @Test
    void testScroll() {
    }

    @Test
    void testScan1() {
    }

    @Test
    void testScroll1() {
    }

    @Test
    void scrollQuery() {
    }

    @Test
    void scanQuery() {
    }

    @Test
    void scrollHits() {
    }
}