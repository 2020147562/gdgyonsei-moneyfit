package com.gdgyonsei.otp.domains.point.repository;

import com.gdgyonsei.otp.domains.point.model.Points;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointsRepository extends JpaRepository<Points, Long> {
    Optional<Points> findByMemberEmail(String memberEmail);
}
