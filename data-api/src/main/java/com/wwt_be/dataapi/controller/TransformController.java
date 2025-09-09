package com.wwt_be.dataapi.controller;

import com.wwt_be.dataapi.dto.TransformRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransformController {
    record TransformResponse(String result) {}

    @PostMapping(value="/transform", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public TransformResponse transform(@RequestBody TransformRequest req) {
        var out = new StringBuilder(req.text()).reverse().toString().toUpperCase();
        return new TransformResponse(out);
    }
}
