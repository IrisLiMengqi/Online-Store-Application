package edu.neu.cs5200.onlineStore.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.cs5200.onlineStore.entities.Book;
import edu.neu.cs5200.onlineStore.repositories.BookRepository;
import edu.neu.cs5200.onlineStore.services.BookService;
@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	BookRepository bookRepository;
	
	@Override
	public List<Book> findAll() {
		return (List<Book>) bookRepository.findAll();
	}

	@Override
	public Book findOne(Long id) {
		return bookRepository.findOne(id);
	}

	@Override
	public List<Book> blurrySearch(String title) {
		List<Book> bookList = bookRepository.findByTitleContaining(title);
		List<Book> activeList = new ArrayList<>();
		
		for (Book book : bookList) {
//			if (book.isActive()) {
				activeList.add(book);
//			}
		}
		return activeList;
	}

}
