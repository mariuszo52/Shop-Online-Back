package com.shoponlineback.cart;

import com.shoponlineback.cartProducts.CartProduct;
import com.shoponlineback.cartProducts.CartProductRepository;
import com.shoponlineback.exceptions.product.ProductNotFoundException;
import com.shoponlineback.product.Product;
import com.shoponlineback.product.ProductRepository;
import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.product.mapper.ProductDtoMapper;
import com.shoponlineback.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.shoponlineback.user.UserService.getLoggedUser;

@Service

public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository,
                       CartProductRepository cartProductRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartProductRepository = cartProductRepository;
    }

    public void addProductToCart(ProductDto productDto) {
        User loggedUser = getLoggedUser();
        Cart cart = loggedUser.getCart();
        Product product = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (cartProductRepository.existsByCart_IdAndProduct_Id(cart.getId(), productDto.getId())) {
            throw new RuntimeException("Product is already in a cart.");
        } else {
            CartProduct cartProduct = new CartProduct(cart, product, 1);
            cartProductRepository.save(cartProduct);
        }
    }

    List<ProductDto> getLoggedUserCart() {
        List<CartProduct> cartProducts = cartProductRepository.findCartProductByCart_id(getLoggedUser().getCart().getId());
        return cartProducts.stream()
                .map(cartProduct -> {
                    ProductDto productDto = ProductDtoMapper.map(cartProduct.getProduct());
                    productDto.setCartQuantity(cartProduct.getQuantity());
                    return productDto;
                })
                .collect(Collectors.toList());
    }

    void updateProductQuantity(ProductDto productDto) {
        Cart cart = cartRepository.findById(getLoggedUser().getCart().getId())
                .orElseThrow(() -> new RuntimeException("Cart not found."));
        CartProduct cartProduct = cartProductRepository.findByCart_IdAndProduct_Id(cart.getId(), productDto.getId())
                .orElseThrow(() -> new RuntimeException("Cart product to update not found."));
        cartProduct.setQuantity(productDto.getCartQuantity());
        cartProductRepository.save(cartProduct);

    }

    @Transactional
    public void clearCart() {
        cartProductRepository.deleteAllByCart_Id(getLoggedUser().getId());
    }

    @Transactional
    public void removeProductFromCart(long id) {
        cartProductRepository.deleteCartProductByCartIdAndProduct_id(getLoggedUser().getCart().getId(), id);
    }

    @Transactional
    public void updateAllCart(List<ProductDto> cartProductsDto) {
        Cart loggedUserCart = getLoggedUser().getCart();
        cartProductRepository.deleteAllByCart_Id(loggedUserCart.getId());
        cartProductsDto.forEach(productDto -> {
            Product product = productRepository.findById(productDto.getId())
                    .orElseThrow(ProductNotFoundException::new);
            CartProduct cartProduct = new CartProduct(loggedUserCart, product, productDto.getCartQuantity());
            cartProductRepository.save(cartProduct);
        });
    }
}
