package kr.hhplus.be.server.app.queue.interfaces.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.app.queue.interfaces.req.IssueQueueTokenRequest;
import kr.hhplus.be.server.app.queue.interfaces.res.IssueQueueTokenResponse;
import kr.hhplus.be.server.app.queue.interfaces.res.TokenPositionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/queue")
@Tag(name = "대기열 토큰", description = "대기열 토큰 관리")
public class QueueController {

    @Operation(
            summary = "대기열 토큰 발급",
            description = "대기열에 진입하기 위해 대기열 토큰을 발급받는다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Queue 토큰이 성공적으로 생성되었습니다.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{ \"id\": 1, \"userId\": 1, \"token\": \"e20d7b49-bf11-41ed-9662-27229eaa91a7\", \"tokenStatus\": \"Waiting\", \"createdAt\": \"2025-01-03T12:00:00\" }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{ \"message\": \"사용자 id를 입력해주세요.\" }")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 발생",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{ \"message\": \"Internal server error\" }")
                    )
            )
    })
    @PostMapping("/token")
    public ResponseEntity<?> createQueueToken(@RequestBody IssueQueueTokenRequest request) {
        String token = UUID.randomUUID().toString();
        IssueQueueTokenResponse response = IssueQueueTokenResponse.builder()
                .id(1L)
                .userId(request.userId())
                .token(token)
                .tokenStatus("Waiting")
                .createdAt(LocalDateTime.now())
                .build();


        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "대기열 토큰 순서 조회",
            description = "인증된 Bearer 토큰을 통해 고정된 대기열 순서를 반환한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "현재 순서 반환",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{ \"token\": \"e20d7b49-bf11-41ed-9662-27229eaa91a7\", \"position\": 1 }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "잘못된 토큰",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{ \"message\": \"유효하지 않은 인증 토큰입니다.\" }")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 발생",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{ \"message\": \"Internal server error\" }")
                    )
            )
    })
    @GetMapping("/token/position")
    public ResponseEntity<?> getQueueTokenPosition() {
        TokenPositionResponse response = new TokenPositionResponse("e20d7b49-bf11-41ed-9662-27229eaa91a7", 1L);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}