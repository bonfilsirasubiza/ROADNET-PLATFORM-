package com.roadnet.repository;

import com.roadnet.model.IntroductionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntroductionRequestRepository extends JpaRepository<IntroductionRequest, Long> {

    List<IntroductionRequest> findByRecipientId(Long recipientId);

    List<IntroductionRequest> findBySenderId(Long senderId);
}
