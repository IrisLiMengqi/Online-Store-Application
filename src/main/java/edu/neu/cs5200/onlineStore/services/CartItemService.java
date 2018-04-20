package edu.neu.cs5200.onlineStore.services;

import java.util.List;

import edu.neu.cs5200.onlineStore.entities.Book;
import edu.neu.cs5200.onlineStore.entities.CartItem;
import edu.neu.cs5200.onlineStore.entities.ShoppingCart;
import edu.neu.cs5200.onlineStore.entities.User;

public interface CartItemService {

	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem);

	CartItem addBookToCartItem(Book book, User user, int qty);

	CartItem findById(Long cartItemId);

	void removeCartItem(CartItem cartItem);

	CartItem save(CartItem cartItem);
}
