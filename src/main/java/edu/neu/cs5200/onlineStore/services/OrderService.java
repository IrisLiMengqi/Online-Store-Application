package edu.neu.cs5200.onlineStore.services;

import edu.neu.cs5200.onlineStore.entities.Order;
import edu.neu.cs5200.onlineStore.entities.ShoppingCart;
import edu.neu.cs5200.onlineStore.entities.User;

public interface OrderService {
	
	Order createOrder(ShoppingCart shoppingCart, String shippingMethod, User user);
	
	Order findOne(Long id);
		
}
