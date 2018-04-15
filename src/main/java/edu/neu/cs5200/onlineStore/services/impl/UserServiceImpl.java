package edu.neu.cs5200.onlineStore.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.cs5200.onlineStore.entities.User;
import edu.neu.cs5200.onlineStore.repositories.PasswordResetTokenRepository;
import edu.neu.cs5200.onlineStore.security.PasswordResetToken;
import edu.neu.cs5200.onlineStore.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
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
}
