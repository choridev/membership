package com.example.springboot.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MembershipErrorResult {

    MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "Membership Not Found"),
    DUPLICATED_MEMBERSHIP(HttpStatus.BAD_REQUEST, "Duplicated Membership Add Request"),
    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Exception"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
