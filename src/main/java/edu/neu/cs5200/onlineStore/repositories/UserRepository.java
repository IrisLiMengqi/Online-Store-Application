package edu.neu.cs5200.onlineStore.repositories;

import org.springframework.data.repository.CrudRepository;

import edu.neu.cs5200.onlineStore.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{
	User findByUsername(String username);
}
