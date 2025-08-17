package com.bits.bytes.bits.bytes.Repo;

import com.bits.bytes.bits.bytes.Models.Profiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilesRepo extends JpaRepository<Profiles, Integer> {
}
