package com.bits.bytes.bits.bytes.Repo;
import com.bits.bytes.bits.bytes.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {
     Users findByUsername(String username);
}
