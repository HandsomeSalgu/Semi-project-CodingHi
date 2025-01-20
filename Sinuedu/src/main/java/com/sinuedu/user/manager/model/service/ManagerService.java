package com.sinuedu.user.manager.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sinuedu.user.manager.model.mapper.ManagerMapper;
import com.sinuedu.user.manager.model.vo.ChapterDTO;
import com.sinuedu.user.member.model.vo.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManagerService {

	private final ManagerMapper mapper;

	public List<Member> userList() {
		return mapper.userList();
	}

	public List<ChapterDTO> chapterList() {
	    return mapper.chapterList();
	}

}
