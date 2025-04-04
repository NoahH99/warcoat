package com.noahhendrickson.warcoat.repository.discord;

import com.noahhendrickson.warcoat.entity.discord.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
