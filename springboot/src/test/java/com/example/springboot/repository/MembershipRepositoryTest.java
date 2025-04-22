package com.example.springboot.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.springboot.entity.MembershipEntity;
import com.example.springboot.entity.MembershipType;

@DataJpaTest
class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    @DisplayName("Save membership successfully")
    void saveMembershipSuccessfully() {
        // given
        final MembershipEntity membershipEntity = MembershipEntity.builder()
                .userId("Kim")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        // when
        final MembershipEntity result = membershipRepository.save(membershipEntity);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("Kim");
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(10000);
    }

    @Test
    @DisplayName("Find membership by user ID and type")
    void findMembershipByUserIdAndType() {
        // given
        final MembershipEntity membershipEntity = MembershipEntity.builder()
                .userId("Kim")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();
        membershipRepository.save(membershipEntity);

        // when
        final Optional<MembershipEntity> foundResult = membershipRepository.findByUserIdAndMembershipType("Kim",
                MembershipType.NAVER);

        // then
        assertThat(foundResult)
                .isPresent()
                .hasValueSatisfying(result -> {
                    assertThat(result.getId()).isNotNull();
                    assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
                    assertThat(result.getPoint()).isEqualTo(10000);
                });
    }

    @Test
    @DisplayName("Find membership by user ID when membership count is 0")
    void findMembershipByUserIdWhenMembershipCountIsZero() {
        // given

        // when
        final List<MembershipEntity> result = membershipRepository.findAllByUserId("Kim");

        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Find membership by user ID when membership count is 2")
    void findMembershipByUserIdWhenMembershipCountIsTwo() {
        // given
        final MembershipEntity naverMembershipEntity = MembershipEntity.builder()
                .userId("Kim")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();
        final MembershipEntity kakaoMembershipEntity = MembershipEntity.builder()
                .userId("Kim")
                .membershipType(MembershipType.KAKAO)
                .point(10000)
                .build();

        membershipRepository.save(naverMembershipEntity);
        membershipRepository.save(kakaoMembershipEntity);

        // when
        final List<MembershipEntity> result = membershipRepository.findAllByUserId("Kim");

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Delete membership")
    void deleteMembership() {
        // given
        final MembershipEntity membershipEntity = MembershipEntity.builder()
                .userId("Kim")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();
        final MembershipEntity savedMembershipEntity = membershipRepository.save(membershipEntity);

        // when
        membershipRepository.deleteById(savedMembershipEntity.getId());

        // then
        final Optional<MembershipEntity> foundMembershipEntity = membershipRepository
                .findById(savedMembershipEntity.getId());
        assertThat(foundMembershipEntity.isEmpty()).isEqualTo(true);
    }
}
