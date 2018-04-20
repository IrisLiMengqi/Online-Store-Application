package edu.neu.cs5200.onlineStore.services.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.cs5200.onlineStore.entities.Book;
import edu.neu.cs5200.onlineStore.entities.BookToCartItem;
import edu.neu.cs5200.onlineStore.entities.CartItem;
import edu.neu.cs5200.onlineStore.entities.Order;
import edu.neu.cs5200.onlineStore.entities.ShoppingCart;
import edu.neu.cs5200.onlineStore.entities.User;
import edu.neu.cs5200.onlineStore.repositories.BookToCartItemRepository;
import edu.neu.cs5200.onlineStore.repositories.CartItemRepository;
import edu.neu.cs5200.onlineStore.services.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService{
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private BookToCartItemRepository bookToCartItemRepository;
	
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		return cartItemRepository.findByShoppingCart(shoppingCart);
	}

	@Override
	public CartItem updateCartItem(CartItem cartItem) {
		BigDecimal bigDecimal= new BigDecimal(cartItem.getBook().getPrice()).multiply(new BigDecimal(cartItem.getQty()));
		
		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		cartItem.setSubTotal(bigDecimal);
		
		cartItemRepository.save(cartItem);
		
		return cartItem;
	}

	@Override
	public CartItem addBookToCartItem(Book book, User user, int qty) {
		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
		//if already exists in the shopping cart, we update it
		for (CartItem cartItem : cartItemList) {
			if (book.getId() == cartItem.getBook().getId()) {
				cartItem.setQty(cartItem.getQty() + qty);
				cartItem.setSubTotal(new BigDecimal(book.getPrice()).multiply(new BigDecimal(cartItem.getQty())));
				cartItemRepository.save(cartItem);
				return cartItem;
			}
		}
		
		//if not existed in the shopping cart
		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setBook(book);
		
		cartItem.setQty(qty);
		cartItem.setSubTotal(new BigDecimal(book.getPrice()).multiply(new BigDecimal(cartItem.getQty())));
		
		cartItem = cartItemRepository.save(cartItem);
		
		BookToCartItem bookToCartItem = new BookToCartItem();
		bookToCartItem.setBook(book);
		bookToCartItem.setCartItem(cartItem);
		bookToCartItemRepository.save(bookToCartItem);
		
		return cartItem;	
		
	}

	@Override
	public CartItem findById(Long cartItemId) {
		return cartItemRepository.findOne(cartItemId);
	}

	@Override
	public void removeCartItem(CartItem cartItem) {
		bookToCartItemRepository.deleteByCartItem(cartItem);
		cartItemRepository.delete(cartItem);
	}

	@Override
	public CartItem save(CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}

	@Override
	public List<CartItem> findByOrder(Order order) {
		return cartItemRepository.findByOrder(order);
	}
	
}
