package com.bezkoder.springjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.User;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
 Optional<User> findByUsername(String username);
//Long findById();

  Boolean existsByUsername(String username);
 @Query("SELECT e.solde  FROM User e where  e.id=:id")
 int getSolde(@Param("id") int id);
 @Transactional
 @Modifying
 @Query("UPDATE User e SET e.solde = :n WHERE e.id= :id")
 void updateSolde(@Param("n") int n, @Param("id") int id);


 Boolean existsByEmail(String email);
}
