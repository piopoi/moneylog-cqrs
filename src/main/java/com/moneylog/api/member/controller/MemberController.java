package com.moneylog.api.member.controller;

import com.moneylog.api.member.dto.MemberCreateRequest;
import com.moneylog.api.member.dto.MemberGetResponse;
import com.moneylog.api.member.service.MemberService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController implements MemberControllerDocs {

    private final MemberService memberService;

    @Override
    @PostMapping("/api/members")
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberCreateRequest memberCreateRequest) {
        Long memberId = memberService.createMember(memberCreateRequest);
        return ResponseEntity.created(URI.create("/api/members/" + memberId)).build();
    }

    @Override
    @GetMapping("/api/members/{memberId}")
    public ResponseEntity<MemberGetResponse> getMember(@PathVariable Long memberId) {
        MemberGetResponse memberGetResponse = memberService.getMember(memberId);
        return ResponseEntity.ok(memberGetResponse);
    }

    @Override
    @DeleteMapping("/api/members/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok().build();
    }
}
