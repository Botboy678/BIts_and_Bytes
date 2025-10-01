package com.bits.bytes.bits.bytes.Repo;

import com.bits.bytes.bits.bytes.Models.Projects;
import com.bits.bytes.bits.bytes.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ProjectRepo extends JpaRepository<Projects, Integer> {
    Optional<Projects> findByTitleAndUserId_Username(String title, String username);
    Set<Projects> findAllByTitle(String title);
    Projects findByTitleAndUserId(String title, Users user);
    void deleteAllByUserId(Users user);
}
