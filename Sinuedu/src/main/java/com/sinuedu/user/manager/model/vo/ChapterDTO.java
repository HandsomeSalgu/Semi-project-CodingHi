package com.sinuedu.user.manager.model.vo;

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
public class ChapterDTO {
    private String cgName;
    private String lecTitle;
    private String chapTitle;
    private Date updateDate;
}
