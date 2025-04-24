package com.example.springboot.dto;

import com.example.springboot.entity.MembershipType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MembershipRequestDto {

    @NotNull
    private MembershipType membershipType;

    @NotNull
    @Min(0)
    private Integer point;

}
