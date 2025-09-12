package com.bits.bytes.bits.bytes.Repo;

import com.bits.bytes.bits.bytes.Models.BugReports;
import com.bits.bytes.bits.bytes.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BugReportsRepo extends JpaRepository<BugReports, Integer> {
    void deleteAllByUser(Users user);
    Set<BugReports> findAllByUser(Users user);
}
