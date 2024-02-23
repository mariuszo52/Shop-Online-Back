package com.shoponlineback.product;

import com.shoponlineback.exceptions.product.ProductNotFoundException;
import com.shoponlineback.genre.Genre;
import com.shoponlineback.order.Order;
import com.shoponlineback.orderProduct.OrderProductRepository;
import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.product.mapper.ProductDtoMapper;
import com.shoponlineback.productGenres.ProductGenres;
import com.shoponlineback.productLanguage.ProductLanguage;
import com.shoponlineback.screenshot.Screenshot;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.Bindable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static com.mysql.cj.conf.PropertyKey.logger;
import static com.mysql.cj.conf.PropertyKey.pedantic;
import static java.lang.Math.*;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final OrderProductRepository orderProductRepository;
    public ProductService(ProductRepository productRepository, OrderProductRepository orderProductRepository) {
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
    }

    ProductDto getProductById(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find product."));
        return ProductDtoMapper.map(product);
    }


    public List<ProductDto> getSimilarProducts(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found."));
        long pages =  productRepository.countAllByPlatformName(product.getPlatform().getName()) / 5;
        Random random = new Random();
        Long page = random.longs(1, 0, pages - 1).boxed().findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find random page number"));
        PageRequest pageRequest = PageRequest.of(toIntExact(page), 5);
        return productRepository.findAllByPlatformName(product.getPlatform().getName(), pageRequest).stream()
                .map(ProductDtoMapper::map)
                .toList();
    }
    @Transactional
    public void updateProductSellQuantity(Order order) {
            orderProductRepository.findOrderProductsByOrderId(order.getId()).forEach(orderProduct -> {
                Product product = orderProduct.getProduct();
                product.setSellQuantity(product.getSellQuantity() + orderProduct.getQuantity());
            });;
    }

    public Page<ProductDto> getProducts(int page, int size, Sort sort, Optional<String> name,
                                        Optional<String> device, Optional<String> platform,
                                        Optional<String> genre, Optional<String> language,
                                        Optional<BigDecimal> minPrice, Optional<BigDecimal> maxPrice) {
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Specification<Product> productSpecification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            name.ifPresent(value -> predicates.add(criteriaBuilder.like(root.get("name"), "%" + value + "%")));
            genre.ifPresent(value -> {
                Join<Product, ProductGenres> joinProductGenres = root.join("productGenres");
                predicates.add(criteriaBuilder.equal(joinProductGenres.get("genre").get("name"), value));
            });
            device.ifPresent(value -> predicates.add(criteriaBuilder.equal(root.get("platform").get("device"), value)));
            platform.ifPresent(value -> predicates.add(criteriaBuilder.equal(root.get("platform"). get("name"), value)));
            language.ifPresent(value -> {
                Join<Product, ProductLanguage> joinProductLanguage = root.join("productLanguages");
                predicates.add(criteriaBuilder.equal(joinProductLanguage.get("language").get("name"), value));
            });

            minPrice.ifPresent(value -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), value)));
            maxPrice.ifPresent(value -> predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), value)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return productRepository.findAll(productSpecification, pageRequest).map(ProductDtoMapper::map);

    }
}
