package com.example.springboot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.springboot.dto.MembershipAddResponseDto;
import com.example.springboot.dto.MembershipDetailResponseDto;
import com.example.springboot.dto.MembershipPointResponseDto;
import com.example.springboot.entity.MembershipEntity;
import com.example.springboot.entity.MembershipType;
import com.example.springboot.exception.MembershipErrorResult;
import com.example.springboot.exception.MembershipException;
import com.example.springboot.repository.MembershipRepository;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @InjectMocks
    private MembershipService membershipService;

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private PointService ratePointService;

    private final Long membershipId = 0L;
    private final String userId = "Kim";
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;
    private final LocalDateTime time = LocalDateTime.parse("2025-04-21T13:55:00");

    @Test
    @DisplayName("Fail to add membership when membership is duplicated")
    void failToAddWhenMembershipIsDuplicated() {
        // given
        doReturn(Optional.of(MembershipEntity.builder().build())).when(membershipRepository)
                .findByUserIdAndMembershipType(userId, membershipType);

        // when
        final MembershipException result = assertThrows(MembershipException.class,
                () -> membershipService.addMembership(userId, membershipType, point));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP);
    }

    @Test
    @DisplayName("Add membership successfully")
    void addMembershipSuccessfully() {
        // given
        doReturn(Optional.empty()).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
        doReturn(membershipEntity()).when(membershipRepository).save(any(MembershipEntity.class));

        // when
        final MembershipAddResponseDto result = membershipService.addMembership(userId, membershipType,
                point);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getMembershipType()).isEqualTo(membershipType);
        assertThat(result.getPoint()).isEqualTo(point);

        // verify
        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
        verify(membershipRepository, times(1)).save(any(MembershipEntity.class));
    }

    @Test
    @DisplayName("Get all membership successfully")
    void getAllMembershipSuccessfully() {
        // given
        doReturn(Arrays.asList(
                membershipEntity(),
                MembershipEntity.builder().build(),
                MembershipEntity.builder().build()))
                .when(membershipRepository).findAllByUserId(userId);

        // when
        final List<MembershipDetailResponseDto> result = membershipService.getMembershipList(userId);

        // then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getId()).isNotNull();
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
        assertThat(result.get(0).getMembershipType()).isEqualTo(membershipType);
        assertThat(result.get(0).getPoint()).isEqualTo(point);
        assertThat(result.get(0).getCreatedAt()).isEqualTo(time);
    }

    @Test
    @DisplayName("Fail to get detail membership when membership does not exist")
    void failToGetDetailMembershipWhenMembershipDoesNotExist() {
        // given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class,
                () -> membershipService.getMembership(membershipId, userId));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    @DisplayName("Fail to get detail membership when user is unauthorized")
    void failToGetDetailMembershipWhenUserIsUnauthorized() {
        // given
        doReturn(Optional.of(membershipEntity())).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class,
                () -> membershipService.getMembership(membershipId, "notOnwer"));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    @DisplayName("Get detail membership successfully")
    void getDetailMembershipSuccessfully() {
        // given
        doReturn(Optional.of(membershipEntity())).when(membershipRepository).findById(membershipId);

        // when
        final MembershipDetailResponseDto result = membershipService.getMembership(membershipId, userId);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getMembershipType()).isEqualTo(membershipType);
        assertThat(result.getPoint()).isEqualTo(point);
        assertThat(result.getCreatedAt()).isEqualTo(time);
    }

    @Test
    @DisplayName("Fail to delete membership when membership does not exist")
    void FailToDeleteMembershipWhenMembershipDoesNotExist() {
        // given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class,
                () -> membershipService.removeMembership(membershipId, userId));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    @DisplayName("Fail to delete membership when user is unauthorized")
    void FailToDeleteMembershipWhenUserIsUnauthorized() {
        // given
        doReturn(Optional.of(membershipEntity())).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class,
                () -> membershipService.removeMembership(membershipId, "notOnwer"));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    @DisplayName("Delete membership successfully")
    void deleteMembershipSuccessfully() {
        // given
        final MembershipEntity membershipEntity = membershipEntity();
        doReturn(Optional.of(membershipEntity)).when(membershipRepository).findById(membershipId);

        // when
        membershipService.removeMembership(membershipId, userId);

        // then

        // verify
        verify(membershipRepository, times(1)).findById(membershipId);
        verify(membershipRepository, times(1)).delete(membershipEntity);
    }

    @Test
    @DisplayName("Fail to add point when membership does not exist")
    void failToAddPointWhenMembershipDoesNotExist() {
        // given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class,
                () -> membershipService.addMembershipPoint(membershipId, userId, point));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    @DisplayName("Fail to add point when user is unauthorized")
    void failToAddPointWhenUserIsUnauthorized() {
        // given
        doReturn(Optional.of(membershipEntity())).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class,
                () -> membershipService.addMembershipPoint(membershipId, "nowOnwer", point));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    @DisplayName("Add point successfully")
    void AddPointSuccessfully() {
        // given
        doReturn(Optional.of(membershipEntity())).when(membershipRepository).findById(membershipId);
        doReturn(point * 1 / 100).when(ratePointService).calculateAmount(point);

        // when
        final MembershipPointResponseDto result = membershipService.addMembershipPoint(membershipId, userId, point);

        // then
        assertThat(result.getMembershipType()).isEqualTo(membershipType);
        assertThat(result.getPoint()).isEqualTo(10100);
    }

    private MembershipEntity membershipEntity() {
        return MembershipEntity.builder()
                .id(0L)
                .userId(userId)
                .membershipType(MembershipType.NAVER)
                .point(point)
                .createdAt(time)
                .build();
    }

}
