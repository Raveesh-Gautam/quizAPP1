package com.quize.conceptile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quize.conceptile.model.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    UserSession findByUserId(String userId);
}
