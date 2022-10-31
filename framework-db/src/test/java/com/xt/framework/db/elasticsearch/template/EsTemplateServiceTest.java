package com.xt.framework.db.elasticsearch.template;

import com.google.common.collect.Lists;
import com.xt.framework.db.FrameworkDbApplicationTest;
import com.xt.framework.db.elasticsearch.template.model.Product;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author tao.xiong
 * @Description es template 测试
 * @Date 2022/1/18 17:48
 */
public class EsTemplateServiceTest extends FrameworkDbApplicationTest {
    @Resource
    private EsTemplateService esTemplateService;

    @Test
    public void testCreateProductIndexBulk() {
        List<Product> products= Lists.newArrayList();
        IntStream.range(11,5000).forEach(id->{
            Product product=new Product();
            product.setId("xt-product-"+id);
            product.setCategory("xt-category-"+id);
            product.setDescription("xt-description-"+id);
            product.setManufacturer("xt-manufacturer-"+id);
            product.setPrice(100d+id);
            product.setQuantity(10+id);
            product.setName("xt-name-"+id);
            products.add(product);
        });
        esTemplateService.createProductIndexBulk(products);
    }

    @Test
    public void testCreateProductIndex() {
        Product product=new Product();
        product.setId("xt-product-");
        product.setCategory("xt-category-");
        product.setDescription("xt-description-");
        product.setManufacturer("xt-manufacturer-");
        product.setPrice(100d);
        product.setQuantity(10);
        product.setName("xt-name-");
        esTemplateService.createProductIndex(product);
    }

    @Test
    public void testFindProductsByBrand() {
        List<Product> products=esTemplateService.findProductsByBrand("xt-manufacturer-10");
        System.out.println(products);
    }

    @Test
    public void testFindByProductName() {
        List<Product> products=esTemplateService.findByProductName("xt-name-10");
        System.out.println(products);
    }

    @Test
    public void testFindByProductPrice() {
        List<Product> products=esTemplateService.findByProductPrice("100d");
        System.out.println(products);
    }

    @Test
    public void testProcessSearch() {
    }

    @Test
    public void testFetchSuggestions() {
    }
}