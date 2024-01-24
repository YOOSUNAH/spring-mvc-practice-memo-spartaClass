package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

// Lombook으로 주입 하는 방법 : @RequiredArgsConstructor  를 달면 final을 생성자로 가져간다. (그래서 어노테이션 달면 bean이 생성된다)
@Component
public class MemoService {

    // @Autowired를 필드 위에 붙이면 private 이어도, 외부에서 memoRepository Bean 객체를 주입해줄 수 있다. (하지만 추천하지는 않는다.)
    private final MemoRepository memoRepository;  // final은 바로 초기화하거나, 생성자로 초기화해야한다! 생성자에서 초기화 가능하다.

    // 메서드로 주입하는 방법
    // @Autowired
    // public void setDi(MemoRepository memoRepository;){
    // this.memoRepository = memoRepository;
    // }

    // 수동으로 주입하는 방법
//    public MemoService(ApplicationContext context){
//        // 1. 'Bean'이름으로 가져오기
//        MemoRepository memoRepository = context.getBean("memoRepository"); // (" ")여기에 bean의 이름을 적어야 한다.
            // 2. 'Bean' 클래스 형식으로 가져오기
//        MemoRepository memoRepository = context.getBean("MemoRepository"); // (" ")여기에 타입, memoRepository의 타입인 MemoRepository
//        this.memoRepository = memoRepository;
//    }

    // 생성자 사용하는 이유 (객체의 불변성을 지켜주려고)
    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;

         // this.memoRepository = new MemoRepository(jdbcTemplate);
        // MemoService가  생성자를 통해서 생성이 될때,jdbcTemplate를 파라미터로 받아오고 memoRepository를 생성한다.
        // method 호출 시마다 memoeRepository를 만들 필요가 없어진다.
        // 일반적으로 생성자에서 초기화되는 필드들은 해당 클래스의 인스턴스가 생성될 때, 한 번 초기화되면 계속해서 재사용된다.
        // 그래서 매번 MemoService의 메소드가 호출될 때마다 새로운 MemoRepository를 만들 필요 가 없이 이미 생성된 것을 계속해서 사용할 수 있다.
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장
        Memo saveMemo = memoRepository.save(memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(saveMemo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        // DB 조회
        return memoRepository.findAll();
    }

    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
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
        // 해당 메모가 DB에 존재하는지 확인
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