package com.example.memo.repository;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public interface MemoRepository extends JpaRepository<Memo, Long> {
    // memo 클래스 id타입을 long타입으로 준다. 스프링데이터 jpa가 자동으로 만들어지는 구현체이고, bean으로 등록이 되어있기 떄문에
    // 애너테이션이 필요 없다. 상속받아서 생성이 된다. 요청을 하고자 하는 sql을 메서드 이름을 사용해서 할 수 있다.
    List<Memo> findAllByOrderByModifiedAtDesc(); // 메서드가 자동완성을 사용해서 만든다. 어떻게 가져 올건지, 정렬할건지 modifiedat으로 정렬 desc 내림차순
    // 메서드 이름 패턴으로 simplejpa메서드에서 구현이 된다.


}
