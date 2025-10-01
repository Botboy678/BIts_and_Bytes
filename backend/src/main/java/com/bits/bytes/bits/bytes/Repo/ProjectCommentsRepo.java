package com.bits.bytes.bits.bytes.Repo;
import com.bits.bytes.bits.bytes.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bits.bytes.bits.bytes.Models.ProjectComments;

@Repository
public interface ProjectCommentsRepo extends JpaRepository<ProjectComments, Integer> {
    void deleteAllByUserId(Users user);
}
