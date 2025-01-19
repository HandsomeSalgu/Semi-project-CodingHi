package com.sinuedu.board.lecture.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Chapter {
	
	public int chapNo;
	public String chapTitle;
	public String chapDetail;
	public Date createDate;
	public Date updateDate;
	public int views;
	public int lecNo;
	public int chapRate;
	
}
