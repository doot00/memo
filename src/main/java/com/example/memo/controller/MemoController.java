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
import com.example.memo.entity.Memo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MemoController {

    private final JdbcTemplate jdbcTemplate;

    public MemoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, memo.getUsername());
                    preparedStatement.setString(2, memo.getContents());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        memo.setId(id);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        // DB 조회
        String sql = "SELECT * FROM memo";

        return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents"); // 해당하는 컬럼의 값을 가져온다.
                return new MemoResponseDto(id, username, contents);
            }
        });
    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findById(id);
        if(memo != null) {
            // memo 내용 수정
            String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?"; // 값을 가져온다.
            jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findById(id); // 날려본다.
        if(memo != null) {
            // memo 삭제
            String sql = "DELETE FROM memo WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    private Memo findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM memo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Memo memo = new Memo();
                memo.setUsername(resultSet.getString("username"));
                memo.setContents(resultSet.getString("contents"));
                return memo;
            } else {
                return null;
            }
        }, id);
    }
}