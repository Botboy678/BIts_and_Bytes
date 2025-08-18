package com.bits.bytes.bits.bytes.Repo;

import com.bits.bytes.bits.bytes.Models.DeveloperBlogComments;
import com.bits.bytes.bits.bytes.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperBlogCommentsRepo extends JpaRepository<DeveloperBlogComments, Integer> {
    void deleteAllByUserId(Users user);
}
