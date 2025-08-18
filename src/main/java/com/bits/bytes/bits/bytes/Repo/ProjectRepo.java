package com.bits.bytes.bits.bytes.Repo;

import com.bits.bytes.bits.bytes.Models.Projects;
import com.bits.bytes.bits.bytes.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends JpaRepository<Projects, Integer> {
    Projects findByTitleAndUserId(String title, Users user);
    void deleteAllByUserId(Users user);
}
