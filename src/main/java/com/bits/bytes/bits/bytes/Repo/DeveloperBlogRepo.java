package com.bits.bytes.bits.bytes.Repo;

import com.bits.bytes.bits.bytes.Models.DeveloperBlog;
import com.bits.bytes.bits.bytes.Models.Projects;
import com.bits.bytes.bits.bytes.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeveloperBlogRepo extends JpaRepository<DeveloperBlog, Integer> {
    DeveloperBlog findByTitleAndUserId(String title, Users user);
    Optional<DeveloperBlog> findByTitleAndUserId_Username(String title, String username);
}
