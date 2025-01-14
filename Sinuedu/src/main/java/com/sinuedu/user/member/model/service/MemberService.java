package com.sinuedu.user.member.model.service;

import org.springframework.stereotype.Service;

import com.sinuedu.user.member.model.mapper.MemberMapper;
import com.sinuedu.user.member.model.vo.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberMapper mapper;

	public Member login(Member m) {
		return mapper.login(m);
	}

}
