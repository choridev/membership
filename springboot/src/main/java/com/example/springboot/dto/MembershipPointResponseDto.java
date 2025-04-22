package com.example.springboot.dto;

import com.example.springboot.entity.MembershipType;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class MembershipPointResponseDto {

    private final Long id;
    private final MembershipType membershipType;
    private final Integer point;

}
