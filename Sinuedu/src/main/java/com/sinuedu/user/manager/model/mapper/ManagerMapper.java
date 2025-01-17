package com.sinuedu.user.manager.model.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.sinuedu.user.member.model.vo.Member;

@Mapper
@Repository
public interface ManagerMapper {
    List<Member> userList();
}