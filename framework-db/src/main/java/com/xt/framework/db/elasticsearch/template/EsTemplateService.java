package com.xt.framework.db.elasticsearch.template;

import com.xt.framework.db.elasticsearch.template.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tao.xiong
 * @Description es 服务
 * @Date 2022/1/18 17:35
 */
@Service
@Slf4j
public class EsTemplateService {
    private static final String PRODUCT_INDEX = "productindex";
    @Resource
    private ElasticsearchOperations elasticsearchOperations;

    public List<String> createProductIndexBulk(final List<Product> products) {

        List<IndexQuery> queries = products.stream().map(product -> new IndexQueryBuilder().withId(product.getId()).withObject(product).build()).collect(Collectors.toList());
        return elasticsearchOperations.bulkIndex(queries, IndexCoordinates.of(PRODUCT_INDEX));
    }

    public String createProductIndex(Product product) {
        IndexQuery indexQuery = new IndexQueryBuilder().withId(product.getId()).withObject(product).build();
        return elasticsearchOperations.index(indexQuery, IndexCoordinates.of(PRODUCT_INDEX));
    }

    public List<Product> findProductsByBrand(final String brandName) {

        QueryBuilder queryBuilder = QueryBuilders.matchQuery("manufacturer", brandName);

        Query searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();

        SearchHits<Product> productHits = elasticsearchOperations.search(searchQuery, Product.class, IndexCoordinates.of(PRODUCT_INDEX));
        return convert(productHits);
    }

    public List<Product> findByProductName(final String productName) {
        Query searchQuery = new StringQuery(
                "{\"match\":{\"name\":{\"query\":\"" + productName + "\"}}}\"");

        SearchHits<Product> products = elasticsearchOperations.search(
                searchQuery,
                Product.class,
                IndexCoordinates.of(PRODUCT_INDEX));
        return convert(products);
    }

    public List<Product> findByProductPrice(final String productPrice) {
        Criteria criteria = new Criteria("price")
                .greaterThan(100.0)
                .lessThan(200.0);

        Query searchQuery = new CriteriaQuery(criteria);

        SearchHits<Product> products = elasticsearchOperations
                .search(searchQuery,
                        Product.class,
                        IndexCoordinates.of(PRODUCT_INDEX));
        return convert(products);
    }

    public List<Product> processSearch(final String query) {
        log.info("Search with query {}", query);

        // 1. Create query on multiple fields enabling fuzzy search
        QueryBuilder queryBuilder =
                QueryBuilders
                        .multiMatchQuery(query, "name", "description")
                        .fuzziness(Fuzziness.AUTO);

        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .build();

        // 2. Execute search
        SearchHits<Product> productHits =
                elasticsearchOperations
                        .search(searchQuery, Product.class,
                                IndexCoordinates.of(PRODUCT_INDEX));

        // 3. Map searchHits to product list
        return convert(productHits);
    }

    public List<String> fetchSuggestions(String query) {
        QueryBuilder queryBuilder = QueryBuilders
                .wildcardQuery("name", query + "*");

        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .withPageable(PageRequest.of(0, 5))
                .build();

        SearchHits<Product> searchSuggestions =
                elasticsearchOperations.search(searchQuery,
                        Product.class,
                        IndexCoordinates.of(PRODUCT_INDEX));

        List<String> suggestions = new ArrayList<String>();

        searchSuggestions.getSearchHits().forEach(searchHit -> {
            suggestions.add(searchHit.getContent().getName());
        });
        return suggestions;
    }
    protected List<Product> convert(SearchHits<Product> productHits){
        List<Product> productMatches = new ArrayList<Product>();
        productHits.forEach(searchHit -> {
            productMatches.add(searchHit.getContent());
        });
        return productMatches;
    }
}
