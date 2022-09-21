package com.xt.framework.db.elasticsearch.template;

import com.xt.framework.db.elasticsearch.core.ElasticSearchTemplate;
import com.xt.framework.db.elasticsearch.template.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author tao.xiong
 * @Description 商品es索引
 * @Date 2022/9/20 18:21
 */
@Service
@Slf4j
public class ProductElasticSearchTemplate extends ElasticSearchTemplate<Product> {
    public ProductElasticSearchTemplate() {
        this.setInnerType(Product.class);
        this.setIndexName("productindex");
    }
}
