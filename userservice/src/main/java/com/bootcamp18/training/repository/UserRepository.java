package com.bootcamp18.training.repository;

import com.bootcamp18.training.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findUserByEmailJPQL(
            @Param("email") String email
    );

    @Query(value = "select * from mst_user u where u.email = :email", nativeQuery = true)
    Optional<UserEntity> findUserByEmailNative(
            @Param("email") String email
    );

    boolean existsByEmail(String email);
}
