package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // json데이터만 던지면 되기 때문에 html 따로 반환 x
@RequestMapping("/api") // 중복되는 패스
public class MemoController {

    private final Map<Long, Memo> memoList = new HashMap<>(); // 데이터 저장소 대신 사용 key, value

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {// 요청은 바디이다. Dto로 요청을 받는다.
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // Memo Max로 Id를 찾아야한다.
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId); //setting

        // DB 저장
        memoList.put(memo.getId(), memo);

        // Entity -> ResponseDto로 변환을 한다.
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);
        return memoResponseDto;

    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        // Map To List로 구현을 한다.
        List<MemoResponseDto> responseList = memoList.values().stream()
                .map(MemoResponseDto::new).toList(); // .stream은 for문처럼 돌려준다. memo를 통해 변환을 한다. 파라미터를 가지는 생성자를 찾는다.
        return responseList;
    }


}
