package com.example.memo.service;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import com.example.memo.repository.MemoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoService { // memoService

    private final MemoRepository memoRepository;

    // 의존성 주입이 필요할 때 등록해준다.
    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {

        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        Memo saveMemo = memoRepository.save(memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);
        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        // DB 조회
        return memoRepository.findAll();

    }

    public Long updateMemo(Long id, MemoRequestDto requestDto) {

        // Memo db update
        Memo memo = memoRepository.findById(id);

        if (memo != null) {
            // memo 내용 수정
            memoRepository.update(id, requestDto);
            return id;

        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    public Long deleteMemo(Long id) {
        Memo memo = memoRepository.findById(id);
        if (memo != null) {
            // memo 삭제
            memoRepository.delete(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
}
