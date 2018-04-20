package edu.neu.cs5200.onlineStore.services;

import edu.neu.cs5200.onlineStore.entities.ShoppingCart;

public interface ShoppingCartService {

	ShoppingCart updateShoppingCart(ShoppingCart shoppingCart);

	void clearShoppingCart(ShoppingCart shoppingCart);
}
