package com.example.UserAuth.repository;

import com.example.UserAuth.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query("SELECT user_role FROM UserRole user_role WHERE user_role.user.id = :userId AND user_role.role.id = :roleId")
    List<UserRole> findByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
}