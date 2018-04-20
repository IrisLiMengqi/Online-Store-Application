package edu.neu.cs5200.onlineStore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.neu.cs5200.onlineStore.entities.Book;
import edu.neu.cs5200.onlineStore.entities.User;
import edu.neu.cs5200.onlineStore.services.BookService;
import edu.neu.cs5200.onlineStore.services.UserService;

@Controller
public class SearchController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookService bookService;
	
	@RequestMapping("/searchBook")
	public String searchBook(
			@ModelAttribute("keyword") String keyword,
			Principal principal,
			Model model
			) {
		
		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		List<Book> bookList = bookService.blurrySearch(keyword);
		
		if (bookList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "bookshelf";
		}
		
		model.addAttribute("bookList", bookList);
		
		return "bookshelf";
	}
	
}













