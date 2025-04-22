package com.example.springboot.controller;

import static com.example.springboot.util.MembershipConstants.USER_ID_HEADER;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dto.MembershipRequestDto;
import com.example.springboot.dto.MembershipAddResponseDto;
import com.example.springboot.dto.MembershipDetailResponseDto;
import com.example.springboot.dto.MembershipPointRequestDto;
import com.example.springboot.dto.MembershipPointResponseDto;
import com.example.springboot.service.MembershipService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipAddResponseDto> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipRequestDto membershipRequestDto) {

        final MembershipAddResponseDto membershipAddResponseDto = membershipService
                .addMembership(userId,
                        membershipRequestDto.getMembershipType(),
                        membershipRequestDto.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED).body(membershipAddResponseDto);
    }

    @GetMapping("/api/v1/memberships")
    public ResponseEntity<List<MembershipDetailResponseDto>> getMembershipList(
            @RequestHeader(USER_ID_HEADER) final String userId) {

        return ResponseEntity.status(HttpStatus.OK).body(membershipService.getMembershipList(userId));
    }

    @GetMapping("/api/v1/memberships/{id}")
    public ResponseEntity<MembershipDetailResponseDto> getMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(membershipService.getMembership(id, userId));
    }

    @DeleteMapping("/api/v1/memberships/{id}")
    public ResponseEntity<Void> removeMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long id) {

        membershipService.removeMembership(id, userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/api/v1/memberships/{id}")
    public ResponseEntity<MembershipPointResponseDto> addMembershipPoint(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long id,
            @RequestBody @Valid final MembershipPointRequestDto membershipPointRequestDto) {

        final MembershipPointResponseDto membershipPointResponseDto = membershipService
                .addMembershipPoint(id, userId, membershipPointRequestDto.getPoint());

        return ResponseEntity.status(HttpStatus.OK).body(membershipPointResponseDto);
    }

}
