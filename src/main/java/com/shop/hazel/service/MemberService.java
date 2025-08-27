package com.shop.hazel.service;

import com.shop.hazel.domain.Member;
import com.shop.hazel.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {

        // 이름 중복 검사
        if (isEmailDuplicate(member.getEmail())) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        // 비밀번호 유효성 검사
        String pw = member.getPassword();
        if (!Pattern.matches("(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,}", pw)) {
            throw new IllegalArgumentException("비밀번호는 대문자, 소문자, 특수문자, 숫자를 각각 하나 이상 포함해야 합니다.");
        }

        // 비밀번호 암호화
        member.setPassword(encryptSHA256(pw));

        memberRepository.save(member);
        return member.getId();
    }

    public boolean isEmailDuplicate(String email) {
        List<Member> members = memberRepository.findByName(email);
        return !members.isEmpty();
    }

    private String encryptSHA256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("암호화 실패", e);
        }
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String email) {
        Member member = findOne(id);
        member.setEmail(email);
    }
}
