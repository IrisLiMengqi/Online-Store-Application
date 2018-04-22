package edu.neu.cs5200.onlineStore;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import edu.neu.cs5200.onlineStore.entities.User;
import edu.neu.cs5200.onlineStore.security.Role;
import edu.neu.cs5200.onlineStore.security.UserRole;
import edu.neu.cs5200.onlineStore.services.UserService;
import edu.neu.cs5200.onlineStore.utility.SecurityUtility;

@SpringBootApplication
public class OnlineStoreApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(OnlineStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		//initialize user
		User user = new User();
		user.setFirstname("David");
		user.setLastname("Su");
		user.setUsername("Sugar");
		user.setPassword(SecurityUtility.passwordEncoder().encode("p"));
		user.setEmail("su.zho@husky.neu.edu");
//		user1.setCustomer(true);
		Set<UserRole> userRoles = new HashSet<>();
		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		userRoles.add(new UserRole(user, role));

		userService.createUser(user, userRoles);
		
		//initialize seller
		User user1 = new User();
//		user1.setFirstname("David");
//		user1.setLastname("Su");
		user1.setUsername("admin");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("admin"));
		user1.setEmail("admin@husky.neu.edu");
		Set<UserRole> userRoles1 = new HashSet<>();
		Role role1 = new Role();
		role1.setRoleId(0); // 0 is admin, 1 is user
		role1.setName("ROLE_ADMIN");
		userRoles1.add(new UserRole(user1, role1));
		userService.createUser(user1, userRoles1);
	
		//initialize customer service
		User user2 = new User();
//		user1.setFirstname("David");
//		user1.setLastname("Su");
		user2.setUsername("service");
		user2.setPassword(SecurityUtility.passwordEncoder().encode("service"));
		user2.setEmail("admin@husky.neu.edu");
	
		Role role2 = new Role();
		role2.setRoleId(2); // 0 is admin, 1 is user
		role2.setName("ROLE_SERVICE");
		Set<UserRole> userRoles2 = new HashSet<>();
		userRoles2.add(new UserRole(user2, role2));
		
		userService.createUser(user2, userRoles2);
		
	}

}
