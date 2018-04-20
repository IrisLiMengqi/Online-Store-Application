package edu.neu.cs5200.onlineStore.services.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.neu.cs5200.onlineStore.entities.ShoppingCart;
import edu.neu.cs5200.onlineStore.entities.User;
import edu.neu.cs5200.onlineStore.repositories.PasswordResetTokenRepository;
import edu.neu.cs5200.onlineStore.repositories.RoleRepository;
import edu.neu.cs5200.onlineStore.repositories.UserRepository;
import edu.neu.cs5200.onlineStore.security.PasswordResetToken;
import edu.neu.cs5200.onlineStore.security.UserRole;
import edu.neu.cs5200.onlineStore.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Override
	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordResetTokenRepository.findByToken(token);
	}
	
	@Override
	public void createPasswordResetTokenForUser(final User user, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public User createUser(User user, Set<UserRole> userRoles) {
		User localUser = userRepository.findByUsername(user.getUsername());
		
		if (localUser != null) {
			LOG.info("user {} User already exists. Nothing will be done.", user.getUsername());
		
		} else {
			for (UserRole ur : userRoles) {
				roleRepository.save(ur.getRole());
			}
			
			user.getUserRoles().addAll(userRoles);
			
			//shopping cart for each user
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setUser(user);
			user.setShoppingCart(shoppingCart);
			
			
			
			localUser = userRepository.save(user);
		}
		
		return localUser;
	}

	@Override
	public User save(User currentUser) {
		return userRepository.save(currentUser);
	}

	@Override
	public User findById(Long id) {
		return userRepository.findOne(id);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
