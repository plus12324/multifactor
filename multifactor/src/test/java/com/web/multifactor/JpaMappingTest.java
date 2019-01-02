package com.web.multifactor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

//import com.web.domain.Board;
//import com.web.domain.enums.BoardType;
import com.web.multifactor.model.User;
import com.web.multifactor.oauth.SocialType;
import com.web.multifactor.repository.jpa.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class JpaMappingTest {
    private final String boardTestTitle = "테스트";
    private final String email = "test@gmail.com";

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    BoardRepository boardRepository;


    @Before
    public void init() {
        userRepository.save(User.builder()
                .name("havi")
                .password("test")
                .email(email)
                .pincipal(".")
                .createddate(LocalDateTime.now())
                .socialtype(SocialType.KAKAO)
                .build());

//        boardRepository.save(Board.builder()
//                .title(boardTestTitle)
//                .subTitle("서브 타이틀")
//                .content("컨텐츠")
//                .boardType(BoardType.free)
//                .createdDate(LocalDateTime.now())
//                .updatedDate(LocalDateTime.now())
//                .user(user).build());
    }

    @Test
    public void jpa_test1() {
        User user = userRepository.findByEmail(email);
        assertThat(user.getName(), is("havi"));
        assertThat(user.getPassword(), is("test"));
        assertThat(user.getEmail(), is(email));

        log.info("■■■■■■■■■■■■■");
//        Board board = boardRepository.findByUser(user);
//        assertThat(board.getTitle(), is(boardTestTitle));
//        assertThat(board.getSubTitle(), is("서브 타이틀"));
//        assertThat(board.getContent(), is("컨텐츠"));
//        assertThat(board.getBoardType(), is(BoardType.free));
    }

}
