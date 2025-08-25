package com.shop.hazel.controller;

import com.shop.hazel.domain.Address;
import com.shop.hazel.domain.Member;
import com.shop.hazel.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Collections;

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

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setPassword(form.getPassword());
        member.setConfirmPassword(form.getConfirmPassword());
        member.setAddress(address);

        try {
            if (!form.getPassword().equals(form.getConfirmPassword())) {
                result.rejectValue("confirmPassword", "passwordMismatch", "비밀번호가 일치하지 않습니다.");
                return "members/createMemberForm";
            }
            memberService.join(member);
        } catch (IllegalStateException e) {
            result.rejectValue("name", "duplicate", e.getMessage());
            return "members/createMemberForm";
        } catch (IllegalArgumentException e) {
            result.rejectValue("password", "invalidPassword", e.getMessage());
            return "members/createMemberForm";
        }

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

    // 실시간 이름 중복 체크
    @GetMapping("/members/check-name")
    @ResponseBody
    public Map<String, Boolean> checkName(@RequestParam String name) {
        boolean exists = memberService.isNameDuplicate(name);
        return Collections.singletonMap("exists", exists);
    }
}
