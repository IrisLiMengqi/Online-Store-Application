package edu.neu.cs5200.onlineStore.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import edu.neu.cs5200.onlineStore.entities.CartItem;
import edu.neu.cs5200.onlineStore.entities.ShoppingCart;

@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Long> {

	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
}
