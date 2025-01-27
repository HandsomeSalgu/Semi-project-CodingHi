package com.sinuedu.user.manager.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Lecture;
import com.sinuedu.user.member.model.vo.Member;

@Mapper
@Repository
public interface ManagerMapper {

	List<Member> userList();

	List<Lecture> lectureList();
	
	List<Chapter> chapterList();

	int deleteChapter(String chapTitle);
}