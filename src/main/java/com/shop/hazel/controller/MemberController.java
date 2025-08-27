package com.shop.hazel.controller;

import com.shop.hazel.domain.Address;
import com.shop.hazel.domain.Member;
import com.shop.hazel.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid @ModelAttribute("memberForm") MemberForm form,
                         BindingResult result,
                         Model model) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "passwordMismatch", "비밀번호가 일치하지 않습니다.");
            return "members/createMemberForm";
        }

        Member member = new Member();
        member.setEmail(form.getEmail());
        member.setPassword(form.getPassword());
        member.setConfirmPassword(form.getConfirmPassword());
        member.setAddress(new Address(form.getAddress(), form.getDetailAddr(), form.getExtraAddr(), form.getZipcode()));

        try {
            memberService.join(member);
        } catch (IllegalStateException e) {
            result.rejectValue("email", "duplicate", e.getMessage());
            return "members/createMemberForm";
        } catch (IllegalArgumentException e) {
            result.rejectValue("password", "invalidPassword", e.getMessage());
            return "members/createMemberForm";
        }

        model.addAttribute("signupSuccess", true);
        return "members/createMemberForm";
    }

    @GetMapping("/members/check-email")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam String email) {
        boolean exists = memberService.isEmailDuplicate(email);
        return Collections.singletonMap("exists", exists);
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
