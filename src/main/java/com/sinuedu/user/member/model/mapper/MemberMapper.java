package com.sinuedu.user.member.model.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.sinuedu.user.member.model.vo.Member;

@Mapper
public interface MemberMapper {

	Member login(Member m);

	int insertMember(Member m);

	int checkId(String userId);
	
	int checkUserNick(String userNick);

	String findMyId(Member m);

	int updateMember(Member m);

}