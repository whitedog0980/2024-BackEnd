package com.example.demo.controller;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.request.MemberCreateRequest;
import com.example.demo.controller.dto.request.MemberUpdateRequest;
import com.example.demo.controller.dto.response.MemberResponse;
import com.example.demo.service.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<MemberResponse> response = memberService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> getMember(
        @PathVariable Long id
    ) {
        try {
            MemberResponse response = memberService.getById(id);
            return ResponseEntity.ok(response);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> create(
        @RequestBody MemberCreateRequest request
    ) {
        if (memberService.isNullExist(request)) {
            return ResponseEntity.badRequest().build();
        }

        MemberResponse response = memberService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(
        @PathVariable Long id,
        @RequestBody MemberUpdateRequest request
    ) {
        if (memberService.isEmailExist(request.email())) {
            return ResponseEntity.status(409).build();
        }

        MemberResponse response = memberService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(
        @PathVariable Long id
    ) {
        if (memberService.isExistArticle(id)) {
            return ResponseEntity.badRequest().build();
        }
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
