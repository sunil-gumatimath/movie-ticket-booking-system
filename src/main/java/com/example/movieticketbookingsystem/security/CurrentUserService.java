package com.example.movieticketbookingsystem.security;

import com.example.movieticketbookingsystem.entity.AppUser;
import com.example.movieticketbookingsystem.entity.Theater;
import com.example.movieticketbookingsystem.entity.TheaterOwner;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.enums.UserRole;
import com.example.movieticketbookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Resolves the authenticated account and answers resource-ownership questions.
 *
 * <p>Role checks ("is this an admin / theater owner?") belong on controllers via
 * {@code @PreAuthorize}. This service covers what roles cannot express: <em>which</em>
 * account is calling and whether it owns the resource being changed.
 */
@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    /** The active (non-deleted) account for the current request. */
    public AppUser currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication is required");
        }
        return userRepository.findByEmail(authentication.getName())
                .filter(user -> !user.isDeleted())
                .orElseThrow(() -> new AccessDeniedException("Current account is not active"));
    }

    public User currentCustomer() {
        if (currentUser() instanceof User user && user.getUserRole() == UserRole.ROLE_USER) {
            return user;
        }
        throw new AccessDeniedException("Only customer accounts can perform this action");
    }

    public TheaterOwner currentTheaterOwner() {
        if (currentUser() instanceof TheaterOwner owner && owner.getUserRole() == UserRole.ROLE_THEATER_OWNER) {
            return owner;
        }
        throw new AccessDeniedException("Only theater owner accounts can perform this action");
    }

    public void requireOwnerOf(Theater theater) {
        TheaterOwner owner = currentTheaterOwner();
        if (theater.getOwner() == null || !owner.getUserId().equals(theater.getOwner().getUserId())) {
            throw new AccessDeniedException("You may only manage your own theaters");
        }
    }

    /** Allows the account holder themselves, or any admin, to act on the given account. */
    public void requireSelfOrAdmin(String targetUserId) {
        AppUser current = currentUser();
        if (!current.getUserId().equals(targetUserId) && current.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new AccessDeniedException("You may only modify your own account");
        }
    }
}
