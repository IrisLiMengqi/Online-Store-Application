package edu.neu.cs5200.onlineStore.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.neu.cs5200.onlineStore.entities.Book;
import edu.neu.cs5200.onlineStore.entities.CartItem;
import edu.neu.cs5200.onlineStore.entities.Order;
import edu.neu.cs5200.onlineStore.entities.User;
import edu.neu.cs5200.onlineStore.security.PasswordResetToken;
import edu.neu.cs5200.onlineStore.security.Role;
import edu.neu.cs5200.onlineStore.security.UserRole;
import edu.neu.cs5200.onlineStore.services.BookService;
import edu.neu.cs5200.onlineStore.services.CartItemService;
import edu.neu.cs5200.onlineStore.services.OrderService;
import edu.neu.cs5200.onlineStore.services.UserService;
import edu.neu.cs5200.onlineStore.services.impl.UserSecurityService;
import edu.neu.cs5200.onlineStore.utility.MailConstructor;
import edu.neu.cs5200.onlineStore.utility.SecurityUtility;

@Controller
public class HomeController {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private UserService userService;

	@Autowired
	private UserSecurityService userSecurityService;

	@Autowired
	private BookService bookService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private CartItemService cartItemService;
	// default index page and customer page
	// @RequestMapping("/")
	// public String index(
	// @RequestParam(value="isLoggedIn", required=false) String isLoggedIn,
	// Model model
	// ) {
	// if (isLoggedIn == null) {
	// return "index";
	// }
	// model.addAttribute("isLoggedIn", true);
	// return "index";
	//
	// }
	//
	// //seller page
	// @RequestMapping("/seller")
	// public String seller() {
	// return "seller";
	// }
	//
	// @RequestMapping("/login")
	// public String login(Model model) {
	// model.addAttribute("classActiveLogin", true);
	// return "myAccount";
	// }
	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}

	@RequestMapping("/bookshelf")
	public String bookshelf(Model model) {
		List<Book> bookList = bookService.findAll();
		model.addAttribute("bookList", bookList);
		return "bookshelf";
	}

	@RequestMapping("/bookDetail")
	public String bookDetail(@PathParam("id") Long id, Model model, Principal principal) {
		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}

		Book book = bookService.findOne(id);
		model.addAttribute("book", book);

		List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		return "bookDetail";
	}

	// @RequestMapping(value="/login", method=RequestMethod.POST)
	// public String login(
	// @ModelAttribute("username") String username,
	// @ModelAttribute("password") String password,
	// RedirectAttributes attr,
	// Model model
	// ) {
	// User user = userService.findByUsername(username);
	//
	// //SecurityUtility.passwordEncoder().encode("p")
	// //&& user.getPassword() == password
	//
	// if (user != null ) {
	// if (user.isCustomer()) {
	// attr.addAttribute("isLoggedIn", "y");
	// return "redirect:/";
	// } else {
	// return "seller";
	// }
	// }
	// model.addAttribute("error", true);
	// return "myAccount";
	//
	// }
	@RequestMapping("/myProfile")
	public String myProfile(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("orderList", user.getOrderList());
		// model.addAttribute("orderList",user.getOrderList());

		model.addAttribute("classActiveEdit", true);

		return "myProfile";

	}

	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
	public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String userEmail,
			@ModelAttribute("username") String username, Model model) throws Exception {
		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", userEmail);
		model.addAttribute("username", username);
		

		if (userService.findByUsername(username) != null) {
			model.addAttribute("usernameExists", true);

			return "myAccount";
		}

		if (userService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);

			return "myAccount";
		}

		User user = new User();
		user.setUsername(username);
		user.setEmail(userEmail);

		String password = SecurityUtility.randomPassword();

		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, role));
		userService.createUser(user, userRoles);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);

		mailSender.send(email);

		model.addAttribute("emailSent", "true");
		model.addAttribute("orderList", user.getOrderList());

		return "myAccount";
	}

	@RequestMapping("/newUser")
	public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
		PasswordResetToken passToken = userService.getPasswordResetToken(token);

		if (passToken == null) {
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		User user = passToken.getUser();
		String username = user.getUsername();

		UserDetails userDetails = userSecurityService.loadUserByUsername(username);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		model.addAttribute("user", user);

		model.addAttribute("classActiveEdit", true);
		model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}

	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
	public String updateUserInfo(@ModelAttribute("user") User user, @ModelAttribute("newPassword") String newPassword,
			Model model) throws Exception {

		User currentUser = userService.findById(user.getId());

		if (currentUser == null) {
			throw new Exception("User not found");
		}

		/* check email already exists */
		if (userService.findByEmail(user.getEmail()) != null) {
			if (userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
				model.addAttribute("emailExists", true);
				return "myProfile";
			}
		}

		/* check username already exists */
		if (userService.findByUsername(user.getUsername()) != null) {
			if (userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
				model.addAttribute("usernameExists", true);
				return "myProfile";
			}
		}

		// update password
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUser.getPassword();
			if (passwordEncoder.matches(user.getPassword(), dbPassword)) {
				currentUser.setPassword(passwordEncoder.encode(newPassword));
			} else {
				model.addAttribute("incorrectPassword", true);

				return "myProfile";
			}
		}

		currentUser.setFirstname(user.getFirstname());
		currentUser.setLastname(user.getLastname());
		currentUser.setUsername(user.getUsername());
		currentUser.setEmail(user.getEmail());

		userService.save(currentUser);

		model.addAttribute("updateSuccess", true);
		model.addAttribute("user", currentUser);
		model.addAttribute("classActiveEdit", true);

		UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
		model.addAttribute("orderList", user.getOrderList());

		return "myProfile";

	}
	
	@RequestMapping("/orderDetail")
	public String orderDetail(
			@RequestParam("id") Long orderId,
			Model model,
			Principal principal
			) {
		
		User user = userService.findByUsername(principal.getName());
		Order order = orderService.findOne(orderId);
		
		if (order.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			List<CartItem> cartItemList = cartItemService.findByOrder(order);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("user", user);
			model.addAttribute("order", order);
			model.addAttribute("orderList", user.getOrderList());
			model.addAttribute("classActiveOrders", true);
			model.addAttribute("displayOrderDetail", true);
			
			return "myProfile";
		}
		
		
		
	}
	
	

}






















