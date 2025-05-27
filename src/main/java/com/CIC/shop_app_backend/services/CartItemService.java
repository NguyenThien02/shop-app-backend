package com.CIC.shop_app_backend.services;

import com.CIC.shop_app_backend.dtos.CartItemsDTO;
import com.CIC.shop_app_backend.entity.Cart;
import com.CIC.shop_app_backend.entity.CartItem;
import com.CIC.shop_app_backend.entity.Product;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.repository.CartItemRepository;
import com.CIC.shop_app_backend.repository.CartRepository;
import com.CIC.shop_app_backend.repository.ProductRepository;
import com.CIC.shop_app_backend.services.Impl.ICartItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public CartItem createCartItem(CartItemsDTO cartItemsDTO) {

        Cart cart = cartRepository.findById(cartItemsDTO.getCartId())
                .orElseThrow(() -> new DataNotFoundException("Not found Cart with ID: " + cartItemsDTO.getCartId()));

        Product product = productRepository.findById(cartItemsDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Not found product with ID: " + cartItemsDTO.getProductId()));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemsDTO.getQuantity());
        CartItem cartItem1 = cartItemRepository.save(cartItem);
        return  cartItem1;
    }

    @Override
    public Page<CartItem> getAllCartItemByCartId(PageRequest pageRequest, Long cartId) {
        return cartItemRepository.findByCart_CartId(cartId, pageRequest);
    }

    @Override
    public void deleteCartItemById(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public List<CartItem> getCartItemByIds(List<Long> cartItemsIds) {
        return cartItemRepository.findAllByCartItemIdIn(cartItemsIds);
    }

}
