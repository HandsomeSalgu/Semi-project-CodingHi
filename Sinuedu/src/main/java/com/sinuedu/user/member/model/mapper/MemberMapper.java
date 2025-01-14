package com.sinuedu.user.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sinuedu.user.member.model.vo.Member;

@Mapper
public interface MemberMapper {

	Member login(Member m);

}
