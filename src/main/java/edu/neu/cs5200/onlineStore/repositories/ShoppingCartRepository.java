package edu.neu.cs5200.onlineStore.repositories;

import org.springframework.data.repository.CrudRepository;

import edu.neu.cs5200.onlineStore.entities.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {

}
