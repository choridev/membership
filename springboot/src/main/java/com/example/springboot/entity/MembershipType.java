package com.example.springboot.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MembershipType {

    NAVER("Naver"),
    LINE("Line"),
    KAKAO("Kakao"),
    ;

    private final String companyName;

}
