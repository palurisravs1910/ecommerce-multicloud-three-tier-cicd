package com.ecommerce.service;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO = new UserDAO();

    /**
     * Register a new user. Returns null on failure, error message on validation fail.
     */
    public String register(String name, String email, String password,
                           String phone, String address) {
        if (name == null || name.trim().isEmpty())     return "Name is required.";
        if (email == null || !email.contains("@"))     return "Valid email is required.";
        if (password == null || password.length() < 6) return "Password must be at least 6 characters.";

        if (userDAO.emailExists(email)) {
            return "Email is already registered.";
        }

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(name.trim(), email.trim().toLowerCase(),
                             hashed, phone, address);
        boolean saved = userDAO.insertUser(user);
        return saved ? null : "Registration failed. Please try again.";
    }

    /**
     * Authenticate user. Returns the User object on success, null on failure.
     */
    public User login(String email, String password) {
        if (email == null || password == null) return null;
        User user = userDAO.findByEmail(email.trim().toLowerCase());
        if (user == null) return null;
        if (!BCrypt.checkpw(password, user.getPassword())) return null;
        return user;
    }

    /**
     * Get user by ID.
     */
    public User getUserById(int id) {
        return userDAO.findById(id);
    }

    /**
     * Update user profile details.
     */
    public boolean updateProfile(int userId, String name, String phone, String address) {
        User user = userDAO.findById(userId);
        if (user == null) return false;
        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);
        return userDAO.updateUser(user);
    }

    /**
     * Change password — verifies current password first.
     */
    public String changePassword(int userId, String currentPassword, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            return "New password must be at least 6 characters.";
        }
        User user = userDAO.findById(userId);
        if (user == null) return "User not found.";
        if (!BCrypt.checkpw(currentPassword, user.getPassword())) {
            return "Current password is incorrect.";
        }
        String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        return userDAO.updatePassword(userId, hashed) ? null : "Password update failed.";
    }
}
