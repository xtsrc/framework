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

    /**
     * ES 结构：
     * 索引 Index ： 文档集合（目录）：相当于sql 的数据库
     * 类型 Type：逻辑分类（章节）：相当于sql 的表
     * 文档 Document：可被索引的基础信息单元，JSON格式（文章）：相当于sql 中的 数据
     * 字段 Field :数据表的字段（标题、开头、结尾），相当于sql 中的字段
     * 映射 Mapping：字段属性设置key、text（链接、图片），相当于sql 中的字段属性
     * 分片 Shards:对索引 水平拆分，相当于sql 中的水平分库 高性能/吞吐量
     * 副本 Replicas：分片拷贝 ，相当于sql 中的主从；高可用性
     * 分配 Allocation：将分片分配给节点 包含主分片和副本（需要master节点拷贝）
     */


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
        product.setPrice(100D);
        product.setName("测试1");
        product.setManufacturer("测试");
        product.setCategory("测试");
        product.setDescription("测试");
        product.setQuantity(10);
        productElasticSearchTemplate.saveOrUpdateById(product);
        User user = new User();
        user.setName("测试用户");
        user.setAddress("测试地址");
        user.setAge(10);
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


    /**
     * must/mustNot 完全匹配,参与score，相当于 AND
     * should：增加score,相当于OR
     * filter:完全匹配,不计算分值，缓存
     * <p>
     * where a=b and b=c
     * <p>
     * term:精确查询，不分词 相当于= {"query":{"term":{"title":"love China"}}} ==> title = "love China"
     * terms:精确查询，不分词 相当于 in  {"query":{"terms":{"title":["love","China"]}}} ==> title in ["love","China"]
     * <p>
     * term 忽略大小写设置： keyword 增加属性 "normalizer": "lowercase_normalizer"
     * term 需要带 keyword 除非分词后不变，不然查询无结果
     * <p>
     * match:分词 相当于 in {"query":{"match":{"title":"love China"}}} ==> title in ["love","China"]
     * match_phrase: 分词 短语搜索，同时出现，相邻 相当于= {"query":{"match_phrase":{"title":"love china"}}} ==> title = "love China"
     */
    @Test
    void searchOne() {
        BoolQueryBuilder builder = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("name.keyword", "xt-name-10"));

        productElasticSearchTemplate.searchOne(builder);
    }

    @Test
    void aggQuery() {
    }

    /**
     * 深度分页：
     * scan 不需要排序
     * scroll 需要排序
     */
    @Test
    void scan() {
    }

    @Test
    void scroll() {
    }
}