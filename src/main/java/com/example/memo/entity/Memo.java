package com.example.memo.entity;


import com.example.memo.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Memo {
    private Long id;
    private String username;
    private String contents;


    public Memo(MemoRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents(); // 이름과 컨텐츠 내용을 가져와서 두개의 필드에 데이터를 넣어준뒤 생성자를 만들어준다.
    }
}