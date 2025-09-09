package com.wwt_be.authapi.controller;


import com.wwt_be.authapi.dto.ProcessRequest;
import com.wwt_be.authapi.dto.ProcessResponse;
import com.wwt_be.authapi.entity.ProcessingLog;
import com.wwt_be.authapi.entity.User;
import com.wwt_be.authapi.repository.ProcessingLogRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;

@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class ProcessController {
    private final ProcessingLogRepository logs;

    @Value("${app.data-api-url}") String dataApiUrl;
    @Value("${app.internal-token}") String internalToken;

    private RestClient client() {
        return RestClient.builder().baseUrl(dataApiUrl).build();
    }

    @PostMapping("/process")
    public ProcessResponse process(@RequestBody @Valid ProcessRequest req,
                                   @RequestAttribute(name = "user", required = false) Object ignored) {
        // має бути user з SecurityContext, поки
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        var user = (User) auth.getPrincipal();

        var response = client().post()
                .uri("/api/transform")
                .header("X-Internal-Token", internalToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(req)
                .retrieve()
                .body(ProcessResponse.class);
        //!!!!!!!!!!!!!!!!
        assert response != null;
        //!!!!!!!!!!!!!!!!
        logs.save(ProcessingLog.builder()
                .user(user)
                .inputText(req.text())
                .outputText(response.result())
                .createdAt(OffsetDateTime.now())
                .build());

        return response;
    }
}
