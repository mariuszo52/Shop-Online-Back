package com.shoponlineback.userProducts;

import com.shoponlineback.exceptions.product.ProductNotFoundException;
import com.shoponlineback.exceptions.user.UserNotFoundException;
import com.shoponlineback.product.Product;
import com.shoponlineback.product.ProductRepository;
import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.product.mapper.ProductDtoMapper;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.shoponlineback.user.UserService.getLoggedUser;

@Service
public class FavoriteProductsService {
    private final FavoriteProductsRepository favoriteProductsRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public FavoriteProductsService(FavoriteProductsRepository favoriteProductsRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.favoriteProductsRepository = favoriteProductsRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public void addProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId()).orElseThrow(ProductNotFoundException::new);
        User user = userRepository.findById(getLoggedUser().getId()).orElseThrow(UserNotFoundException::new);
        LocalDateTime addTime = LocalDateTime.now();
        favoriteProductsRepository.save(new UserProducts(product, user, addTime));
    }

    public List<ProductDto> getUserFavoriteProducts() {
       return favoriteProductsRepository.findAllByUser_Id(getLoggedUser().getId()).stream()
                .map(UserProducts::getProduct)
                .map(ProductDtoMapper::map)
                .toList();
    }
}
