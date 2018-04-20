package edu.neu.cs5200.onlineStore.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.neu.cs5200.onlineStore.entities.Book;

public interface BookRepository extends CrudRepository<Book, Long>{

	//Spring elastic search fuzzy search
	List<Book> findByTitleContaining(String title);

	

}
