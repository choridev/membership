package com.example.springboot.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static com.example.springboot.util.MembershipConstants.USER_ID_HEADER;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.springboot.dto.MembershipRequestDto;
import com.example.springboot.dto.MembershipAddResponseDto;
import com.example.springboot.dto.MembershipDetailResponseDto;
import com.example.springboot.dto.MembershipPointRequestDto;
import com.example.springboot.entity.MembershipType;
import com.example.springboot.exception.GlobalExceptionHandler;
import com.example.springboot.exception.MembershipErrorResult;
import com.example.springboot.exception.MembershipException;
import com.example.springboot.service.MembershipService;
import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    @InjectMocks
    private MembershipController membershipController;

    @Mock
    private MembershipService membershipService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(membershipController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        gson = new Gson();
    }

    @Test
    @DisplayName("Fail to add membership when header is missing")
    void failToAddMembershipWhenHeaderIsMissing() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(membershipRequestDto(MembershipType.NAVER, 10000)))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidMembershipAddParameter")
    @DisplayName("Fail to add membership when request parameters are invalid")
    void failToAddMembershipWhenInvalidParams(MembershipType membershipType, Integer point) throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "Kim")
                        .content(gson.toJson(membershipRequestDto(membershipType, point)))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidMembershipAddParameter() {
        return Stream.of(
                Arguments.of(null, 10000),
                Arguments.of(MembershipType.NAVER, null),
                Arguments.of(MembershipType.NAVER, -1));
    }

    @Test
    @DisplayName("Fail to Add membership when membership already exists")
    void failToAddMembershipWhenServiceThrowsException() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP))
                .when(membershipService)
                .addMembership("Kim", MembershipType.NAVER, 10000);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "Kim")
                        .content(gson.toJson(membershipRequestDto(MembershipType.NAVER, 10000)))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Add membership successfully when request is valid")
    void AddMembershipSuccessfullyWhenValidRequest() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        final MembershipAddResponseDto membershipAddResponseDto = MembershipAddResponseDto.builder()
                .id(0L)
                .membershipType(MembershipType.NAVER)
                .build();

        doReturn(membershipAddResponseDto)
                .when(membershipService)
                .addMembership("Kim", MembershipType.NAVER, 10000);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "Kim")
                        .content(gson.toJson(membershipRequestDto(MembershipType.NAVER, 10000)))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());

        final MembershipAddResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                MembershipAddResponseDto.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
    }

    @Test
    @DisplayName("Fail to get membership when header is missing")
    void failToGetMembershipWhenHeaderIsMissing() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get all membership successfully")
    void getAllMembershipSuccessfully() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        doReturn(Arrays.asList(
                MembershipDetailResponseDto.builder().build(),
                MembershipDetailResponseDto.builder().build(),
                MembershipDetailResponseDto.builder().build()))
                .when(membershipService).getMembershipList("Kim");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url).header(USER_ID_HEADER, "Kim"));

        // then
        resultActions.andExpect(status().isOk());

    }

    @Test
    @DisplayName("Fail to get detail membership when header is missing")
    void failToGetDetailMembershipWhenHeaderIsMissing() throws Exception {
        // given
        final String url = "/api/v1/memberships/0";

        // when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Fail to get detail membership when membership does not exist")
    void failToGetDetailMembershipWhenMembershipDoesNotExist() throws Exception {
        // given
        final String url = "/api/v1/memberships/0";
        doThrow(new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND))
                .when(membershipService)
                .getMembership(0L, "Kim");

        // when
        final ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get(url).header(USER_ID_HEADER, "Kim"));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Fail to get detail membership when user is unauthorized")
    void failToGetDetailMembershipWhenUserIsUnauthorized() throws Exception {
        // given
        final String url = "/api/v1/memberships/0";
        doAnswer(invocation -> {
            String userId = invocation.getArgument(1);
            if (!userId.equals("Kim")) {
                throw new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
            }
            return null;
        }).when(membershipService).getMembership(eq(0L), anyString());

        // when
        final ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get(url).header(USER_ID_HEADER, "notOwner"));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get detail membership successfully")
    void getDetailMembershipSuccessfully() throws Exception {
        // given
        final String url = "/api/v1/memberships/0";
        doReturn(MembershipDetailResponseDto.builder().build())
                .when(membershipService).getMembership(0L, "Kim");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url).header(USER_ID_HEADER, "Kim"));

        // then
        resultActions.andExpect(status().isOk());

    }

    @Test
    @DisplayName("Fail to delete membership when header is missing")
    void failToDeleteMembershipWhenHeaderIsMissing() throws Exception {
        // given
        final String url = "/api/v1/memberships/0";

        // when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(url));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Fail to delete membership when membership does not exist")
    void failToDeleteMembershipWhenMembershipDoesNotExist() throws Exception {
        // given
        final String url = "/api/v1/memberships/0";
        doThrow(new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND))
                .when(membershipService)
                .removeMembership(0L, "Kim");

        // when
        final ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.delete(url).header(USER_ID_HEADER, "Kim"));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Fail to delete membership when user is unauthorized")
    void failToDeleteMembershipWhenUserIsUnauthorized() throws Exception {
        // given
        final String url = "/api/v1/memberships/0";
        doAnswer(invocation -> {
            String userId = invocation.getArgument(1);
            if (!userId.equals("Kim")) {
                throw new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
            }
            return null;
        }).when(membershipService).removeMembership(eq(0L), anyString());

        // when
        final ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.delete(url).header(USER_ID_HEADER, "notOwner"));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete membership successfully")
    void deleteMembershipSuccessfully() throws Exception {
        // given
        final String url = "/api/v1/memberships/0";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
                        .header(USER_ID_HEADER, "Kim"));

        // then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Fail to add point when header is missing")
    void FailToAddPointWhenHeaderIsMissing() throws Exception {
        // given
        final String url = "/api/v1/memberships/0";

        // when
        final ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch(url)
                        .content(gson.toJson(membershipPointRequestDto(10000)))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Fail to add point when membership does not exist")
    void failToAddPointWhenMembershipDoesNotExist() throws Exception {
        // given
        final String url = "/api/v1/memberships/0";
        doThrow(new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND))
                .when(membershipService)
                .addMembershipPoint(0L, "Kim", 10000);

        // when
        final ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch(url)
                        .header(USER_ID_HEADER, "Kim")
                        .content(gson.toJson(membershipPointRequestDto(10000)))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Fail to add point when user is unauthorized")
    void failToAddPointWhenUserIsUnauthorized() throws Exception {
        // given
        final String url = "/api/v1/memberships/0";
        doAnswer(invocation -> {
            String userId = invocation.getArgument(1);
            if (!userId.equals("Kim")) {
                throw new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
            }
            return null;
        }).when(membershipService).addMembershipPoint(eq(0L), anyString(), eq(10000));

        // when
        final ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch(url)
                        .header(USER_ID_HEADER, "notOwner")
                        .content(gson.toJson(membershipPointRequestDto(10000)))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Fail to add point when negative point")
    void failToAddPointWhenNegativePoint() throws Exception {
        // given
        final String url = "/api/v1/memberships/0";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(url)
                        .header(USER_ID_HEADER, "Kim")
                        .content(gson.toJson(membershipPointRequestDto(-10000)))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Add point successfully")
    void AddPointSuccessfully() throws Exception {
        // given
        final String url = "/api/v1/memberships/0";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(url)
                        .header(USER_ID_HEADER, "Kim")
                        .content(gson.toJson(membershipPointRequestDto(10000)))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }

    private MembershipRequestDto membershipRequestDto(final MembershipType membershipType, final Integer point) {
        return MembershipRequestDto.builder()
                .membershipType(membershipType)
                .point(point)
                .build();
    }

    private MembershipPointRequestDto membershipPointRequestDto(final Integer point) {
        return MembershipPointRequestDto.builder()
                .point(point)
                .build();
    }
}
