package com.sinuedu.user.manager.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sinuedu.user.manager.model.mapper.ManagerMapper;
import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.user.member.model.vo.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManagerService {

	private final ManagerMapper mapper;

	public List<Member> userList() {
		return mapper.userList();
	}

	public List<Chapter> chapterList() {
	    return mapper.chapterList();
	}

}
