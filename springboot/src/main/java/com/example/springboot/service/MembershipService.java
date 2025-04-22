package com.example.springboot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.dto.MembershipAddResponseDto;
import com.example.springboot.dto.MembershipDetailResponseDto;
import com.example.springboot.dto.MembershipPointResponseDto;
import com.example.springboot.entity.MembershipEntity;
import com.example.springboot.entity.MembershipType;
import com.example.springboot.exception.MembershipErrorResult;
import com.example.springboot.exception.MembershipException;
import com.example.springboot.repository.MembershipRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final PointService ratePointService;

    @Transactional
    public MembershipAddResponseDto addMembership(final String userId,
            final MembershipType membershipType, final Integer point) {

        membershipRepository
                .findByUserIdAndMembershipType(userId, membershipType)
                .ifPresent(m -> {
                    throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP);
                });

        final MembershipEntity membershipEntity = MembershipEntity.builder()
                .userId(userId)
                .membershipType(membershipType)
                .point(point)
                .build();

        final MembershipEntity savedMembershipEntity = membershipRepository.save(membershipEntity);

        return MembershipAddResponseDto.builder()
                .id(savedMembershipEntity.getId())
                .userId(savedMembershipEntity.getUserId())
                .membershipType(savedMembershipEntity.getMembershipType())
                .point(savedMembershipEntity.getPoint())
                .build();
    }

    public List<MembershipDetailResponseDto> getMembershipList(final String userId) {

        final List<MembershipEntity> allMembership = membershipRepository.findAllByUserId(userId);

        return allMembership.stream()
                .map(v -> MembershipDetailResponseDto.builder()
                        .id(v.getId())
                        .userId(v.getUserId())
                        .membershipType(v.getMembershipType())
                        .point(v.getPoint())
                        .createdAt(v.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

    }

    public MembershipDetailResponseDto getMembership(final Long membershipId, final String userId) {

        final MembershipEntity membershipEntity = getVerifiedMembership(membershipId, userId);

        return MembershipDetailResponseDto.builder()
                .id(membershipEntity.getId())
                .userId(membershipEntity.getUserId())
                .membershipType(membershipEntity.getMembershipType())
                .point(membershipEntity.getPoint())
                .createdAt(membershipEntity.getCreatedAt())
                .build();
    }

    @Transactional
    public void removeMembership(final Long membershipId, final String userId) {

        final MembershipEntity membershipEntity = getVerifiedMembership(membershipId, userId);
        membershipRepository.delete(membershipEntity);

    }

    @Transactional
    public MembershipPointResponseDto addMembershipPoint(final Long membershipId, final String userId,
            final int price) {

        final MembershipEntity membershipEntity = getVerifiedMembership(membershipId, userId);
        final int additionalAmount = ratePointService.calculateAmount(price);
        membershipEntity.setPoint(membershipEntity.getPoint() + additionalAmount);

        return MembershipPointResponseDto.builder()
                .id(membershipEntity.getId())
                .membershipType(membershipEntity.getMembershipType())
                .point(membershipEntity.getPoint())
                .build();

    }

    private MembershipEntity getVerifiedMembership(final Long membershipId, final String userId) {
        return membershipRepository.findById(membershipId)
                .filter(m -> m.getUserId().equals(userId))
                .orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));
    }

}
