package com.example.memo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 해당 추상 클래스를 상속할 경우, 선언한 멤버 변수를 컬럼으로 인식해준다.
@EntityListeners(AuditingEntityListener.class) // 타임 스탬프라는 클래스의 auditing수행된다.
public abstract class Timestamped { // abstract 클래스로 걸어주는게 좋다.

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt; // 시간값이 자동으로 저장된다.

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt; // 마지막에도 변경이 될 때마다 저장이 된다.


}