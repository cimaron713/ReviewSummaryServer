package com.capstone.reviewsummary.apiPayload.code.status;

import com.capstone.reviewsummary.apiPayload.code.BaseErrorCode;
import com.capstone.reviewsummary.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    // 에러
    FAIL_CRAWLING(HttpStatus.BAD_REQUEST, "MEMBER4001", "크롤링 실패했어용용 힝구리."),
    // 멤버 관려 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),

    // 예시,,,
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4001", "게시글이 없습니다."),

    // Ror test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

    // FoodCategory Error
    FOOD_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "FOOD_CATEGORY4001", "음식 카테고리가 없습니다."),
    // User
    DUPLICATED_EMAIL_OR_NICKNAME(HttpStatus.BAD_REQUEST, "USER_400_1", "이메일 혹은 닉네임이 중복입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "USER_400_2", "사용자의 비밀번호가 잘못되었습니다."),
    INVALID_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "USER_400_3", "사용자의 로그인 타입이 잘못되었습니다. 소셜 로그인을 이용해주세요"),
    USER_ALREADY_SUBSCRIBED(HttpStatus.BAD_REQUEST, "USER_400_4", "이미 가입된 사용자입니다."),

    USER_NOT_FOUNDED(HttpStatus.NOT_FOUND, "USER_404_1", "사용자를 찾을 수 없습니다."),
    DO_NOT_CHANGE_PRODUCT(HttpStatus.BAD_REQUEST, "USER_400_5", "제품을 수정할 수 없습니다."),

    // token
    TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "TOKEN_400_1", "헤더에 토큰이 존재하지 않음"),
    TOKEN_UNABLE_TO_EXTRACT(HttpStatus.BAD_REQUEST, "TOKEN_400_2", "토큰을 추출할 수 없음"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_401_1", "토큰이 만료됨"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "TOKEN_401_2", "토큰이 유효하지 않음"),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "TOKEN_401_3", "지원하지 않는 토큰 타입임"),
    ID_TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "TOKEN_400_3", "id_token 이 만료되었거나 유효하지 않음."),

    // Store Error

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_4001","가게가 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}