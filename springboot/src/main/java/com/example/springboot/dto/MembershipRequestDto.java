package com.example.springboot.dto;

import com.example.springboot.entity.MembershipType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class MembershipRequestDto {

    @NotNull
    private final MembershipType membershipType;

    @NotNull
    @Min(0)
    private final Integer point;

}
