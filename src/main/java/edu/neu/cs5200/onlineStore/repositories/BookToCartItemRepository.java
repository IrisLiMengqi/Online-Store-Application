package edu.neu.cs5200.onlineStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import edu.neu.cs5200.onlineStore.entities.BookToCartItem;
import edu.neu.cs5200.onlineStore.entities.CartItem;

@Transactional
public interface BookToCartItemRepository extends CrudRepository<BookToCartItem, Long> {

	void deleteByCartItem(CartItem cartItem);

}
