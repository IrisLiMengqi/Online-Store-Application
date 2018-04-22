package edu.neu.cs5200.onlineStore.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinary.utils.ObjectUtils;

import edu.neu.cs5200.onlineStore.config.CloudinaryConfig;
import edu.neu.cs5200.onlineStore.entities.Book;
import edu.neu.cs5200.onlineStore.services.BookService;




@Controller
@RequestMapping("/book")
public class BookController {
	@Autowired
	private BookService bookService;
	 
	@Autowired
	private CloudinaryConfig cloudc;
	
	
	@RequestMapping("/add")
	public String addBook(Model model) {
		Book book = new Book();
		model.addAttribute("book", book);
		return "addBook";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String addBookPost(
			@ModelAttribute("book") Book book,
			@RequestParam("file") MultipartFile file, 
			RedirectAttributes redirectAttributes
			) {
		
		try {
            Map uploadResult =  cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            String image =  uploadResult.get("url").toString();
            book.setImage(image);
         //   model.addAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
//            model.addAttribute("imageurl", uploadResult.get("url"));
        } catch (IOException e){
            e.printStackTrace();
//            model.addAttribute("message", "Sorry I can't upload that!");
        }
	
		
		bookService.save(book);
	
		return "redirect:/book/bookList";
		
	}

	// book list
	@RequestMapping("/bookList")
	public String booklist(Model model) {
		List<Book> bookList = bookService.findAll();
		model.addAttribute("bookList", bookList);
		return "bookList";
	}
	
	@RequestMapping("/bookInfo")
	public String bookInfo (@RequestParam("id") Long id, Model model) {
		Book book = bookService.findOne(id);
		model.addAttribute("book", book);
		
		return "bookInfo";
	}
	
	@RequestMapping("/updateBook")
	public String updateBook(
			@RequestParam("id") Long id,
			Model model
			) {
		Book book = bookService.findOne(id);
		model.addAttribute("book", book);
		
		return "updateBook";
		
	}
	
	@RequestMapping(value="/updateBook", method=RequestMethod.POST)
	public String updateBookPost(
			@ModelAttribute("book") Book book,
			@RequestParam(value="file", required=false) MultipartFile file, 
			RedirectAttributes redirectAttributes
			) {
		
	
		
		if (!file.isEmpty()) {
			try {
	            Map uploadResult =  cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
	            String image =  uploadResult.get("url").toString();
	            book.setImage(image);
	         //   model.addAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
//	            model.addAttribute("imageurl", uploadResult.get("url"));
	        } catch (IOException e){
	            e.printStackTrace();
//	            model.addAttribute("message", "Sorry I can't upload that!");
	        }
		} else {
			Book curBook = bookService.findOne(book.getId());
			book.setImage(curBook.getImage());
		}
		
		bookService.save(book);
		
		return "redirect:/book/bookInfo?id=" + book.getId();
	
	}
	
	
	@RequestMapping(value="/remove", method=RequestMethod.POST)
	public String remove(
			@ModelAttribute("id") Long id,
			Model model
			) {
		bookService.removeOne(id);
		List<Book> bookList = bookService.findAll();
		model.addAttribute("bookList", bookList);
		
		return "redirect:/book/bookList";
		
		
	}
	
	
	
}













