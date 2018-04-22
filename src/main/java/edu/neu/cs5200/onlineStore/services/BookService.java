package edu.neu.cs5200.onlineStore.services;

import java.util.List;
import java.util.Optional;

import edu.neu.cs5200.onlineStore.entities.Book;

public interface BookService {

	List<Book> findAll();

	Book findOne(Long id);

	List<Book> blurrySearch(String title);
	
	Book save (Book book);
	
	void removeOne(Long id);
}
