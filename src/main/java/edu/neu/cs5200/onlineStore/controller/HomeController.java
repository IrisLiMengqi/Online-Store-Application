package edu.neu.cs5200.onlineStore.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.neu.cs5200.onlineStore.entities.User;
import edu.neu.cs5200.onlineStore.security.PasswordResetToken;
import edu.neu.cs5200.onlineStore.services.UserService;
import edu.neu.cs5200.onlineStore.services.impl.UserSecurityService;

@Controller
public class HomeController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserSecurityService userSecurityService;
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}
	
	@RequestMapping("/newUser")
	public String newUser(
			Locale locale,
			@RequestParam("token") String token,
			Model model) {
		PasswordResetToken passToken = userService.getPasswordResetToken(token);
		
		if (passToken == null) {
			String message = "Invalid Token";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}
		
		User user = passToken.getUser();
		String username = user.getUsername();
		
		UserDetails userDetails = userSecurityService.loadUserByUsername(username);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		model.addAttribute("classActiveEdit", true);
		return "myProfile";
	}


}
