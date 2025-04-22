package com.example.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springboot.entity.MembershipEntity;
import com.example.springboot.entity.MembershipType;

@Repository
public interface MembershipRepository extends JpaRepository<MembershipEntity, Long> {

    Optional<MembershipEntity> findByUserIdAndMembershipType(final String userId, final MembershipType membershipType);

    List<MembershipEntity> findAllByUserId(String userId);

}
