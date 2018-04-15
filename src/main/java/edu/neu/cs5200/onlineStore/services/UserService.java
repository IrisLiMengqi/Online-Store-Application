package edu.neu.cs5200.onlineStore.services;

import edu.neu.cs5200.onlineStore.entities.User;
import edu.neu.cs5200.onlineStore.security.PasswordResetToken;

public interface UserService {
	PasswordResetToken getPasswordResetToken(final String token);
	
	void createPasswordResetTokenForUser(final User user, final String token);
}
