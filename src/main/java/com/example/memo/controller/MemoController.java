//package com.example.memo.controller;
//
//import com.example.memo.dto.MemoRequestDto;
//import com.example.memo.dto.MemoResponseDto;
//import com.example.memo.entity.Memo;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController // json데이터만 던지면 되기 때문에 html 따로 반환 x
//@RequestMapping("/api") // 중복되는 패스
//public class MemoController {
//
//    private final Map<Long, Memo> memoList = new HashMap<>(); // 데이터 저장소 대신 사용 key, value
//
//    @PostMapping("/memos")
//    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {// 요청은 바디이다. Dto로 요청을 받는다.
//        // RequestDto -> Entity
//        Memo memo = new Memo(requestDto);
//
//        // Memo Max로 Id를 찾아야한다.
//        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
//        memo.setId(maxId); //setting
//
//        // DB 저장
//        memoList.put(memo.getId(), memo);
//
//        // Entity -> ResponseDto로 변환을 한다.
//        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);
//        return memoResponseDto;
//
//    }
//
//    @GetMapping("/memos")
//    public List<MemoResponseDto> getMemos() {
//        // Map To List로 구현을 한다.
//        List<MemoResponseDto> responseList = memoList.values().stream()
//                .map(MemoResponseDto::new).toList(); // .stream은 for문처럼 돌려준다. memo를 통해 변환을 한다. 파라미터를 가지는 생성자를 찾는다.
//        return responseList;
//    }
//
//    // api두게 이어서 put mapping을 한다.
//    @PutMapping("/memos/{id}")
//    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
//        // json데이터를 넘겨준다. 메모가 db에존재하는지 확인해야 한다.
//        if (memoList.containsKey(id)) {
//            // true 반환값이 있다. 해당 메모를 가져옴
//            Memo memo = memoList.get(id);
//            // memo수정
//            memo.update(requestDto);
//            return memo.getId(); // id를 리턴한다.
//        } else {
//            throw new IllegalArgumentException("선택한 메모는 존재하지 않는다.");
//        }
//        // key부분에 해당하는 값이
//    }
//
//    // delete
//    @DeleteMapping("/memeos/{id}")
//    public Long deleteMemo(@PathVariable Long id) {
//        // 삭제기 때문에 id만 받는다.
//        if (memoList.containsKey(id)) {
//            memoList.remove(id);
//            return id;
//        } else {
//            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
//        }
//    }
//    //
//}


package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.service.MemoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MemoController {

    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        return memoService.createMemo(requestDto);
        // 컨트롤러에서는 메모서비스만 반환한다.
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        return memoService.getMemos();
    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        return memoService.updateMemo(id, requestDto);
        // 해당 메모가 DB에 존재하는지 확인

    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        return memoService.deleteMemo(id);
        // 해당 메모가 DB에 존재하는지 확인
    }
}