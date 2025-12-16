package com.resume.resumematching.repository;

import com.resume.resumematching.entity.MatchJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchJobRepository extends JpaRepository<MatchJob, Long> {
}

