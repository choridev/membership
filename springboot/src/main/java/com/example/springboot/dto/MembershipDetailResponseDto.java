package com.example.springboot.dto;

import java.time.LocalDateTime;

import com.example.springboot.entity.MembershipType;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class MembershipDetailResponseDto {

    private final Long id;
    private final String userId;
    private final MembershipType membershipType;
    private final Integer point;
    private final LocalDateTime createdAt;

}
