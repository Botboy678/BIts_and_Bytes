package com.bits.bytes.bits.bytes.Repo;

import com.bits.bytes.bits.bytes.Models.DeveloperBlogComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeveloperBlogCommentsRepo extends JpaRepository<DeveloperBlogComments, Integer> {
    List<DeveloperBlogComments> findByDescriptionAndUser_Username(String description, String username);
}
