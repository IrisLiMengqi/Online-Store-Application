package edu.neu.cs5200.onlineStore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.neu.cs5200.onlineStore.entities.CartItem;
import edu.neu.cs5200.onlineStore.entities.Message;
import edu.neu.cs5200.onlineStore.entities.Order;
import edu.neu.cs5200.onlineStore.services.CartItemService;
import edu.neu.cs5200.onlineStore.services.MessageService;
import edu.neu.cs5200.onlineStore.services.OrderService;
import edu.neu.cs5200.onlineStore.services.UserService;


@Controller
@RequestMapping("/adminportal")
public class AdminHomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private MessageService messageService;
	

	@RequestMapping("/")
	public String index() {
		return "redirect:/adminportal/home";
	
	}

	@RequestMapping("/service")
	public String service(Model model) {
		return "adminService";
	}
	
	@RequestMapping("/messageBox")
	public String viewMessage(Model model) {
		
		List<Message> messageList = messageService.findAll();
		
		model.addAttribute("classActiveMessage", true);
		model.addAttribute("messageList", messageList);
		
		return "adminService";
	}
	

	@RequestMapping("/home")
	public String home(Principal principal) {
		if (principal.getName().equals("service")) {
			return "redirect:/adminportal/service";
		} else {
			return "home";
		}
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/orderList")
	public String orderList(Model model) {

		List<Order> orderList = orderService.findAll();
		model.addAttribute("orderList", orderList);
		model.addAttribute("classActiveOrders", true);
		return "orderInfo";
	}

	@RequestMapping("/orderDetail")
	public String orderDetail(@RequestParam("id") Long orderId, Model model) {

		Order order = orderService.findOne(orderId);
		List<Order> orderList = orderService.findAll();
		List<CartItem> cartItemList = cartItemService.findByOrder(order);

		model.addAttribute("orderList", orderList);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("order", order);
		model.addAttribute("classActiveOrders", true);
		model.addAttribute("displayOrderDetail", true);

		return "orderInfo";
	}

		//
		// if (order.getUser().getId() != user.getId()) {
		// return "badRequestPage";
		// } else {
		// List<CartItem> cartItemList = cartItemService.findByOrder(order);
		// model.addAttribute("cartItemList", cartItemList);
		// model.addAttribute("user", user);
		// model.addAttribute("order", order);
		// model.addAttribute("orderList", user.getOrderList());
		// model.addAttribute("classActiveOrders", true);
		// model.addAttribute("displayOrderDetail", true);
		//
		// return "orderInfo";
		// }

	
}
