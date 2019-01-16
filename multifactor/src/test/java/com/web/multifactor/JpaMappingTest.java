package com.web.multifactor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    	for(int i=0 ; i<100 ; i++) {
    		userRepository.save(User.builder()
                    .name("havi"+i)
                    .password("test"+i)
                    .email(email+i)
                    .pincipal(".")
                    .createdDate(LocalDateTime.now())
                    .socialType(SocialType.KAKAO)
                    .build());
    	}
        

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
    @Ignore
    public void jpa_test1() {
        User user = userRepository.findOneByPincipal(".");
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
    
    @Test
    public void jpa_test2() {
    	int pageSize = 10;
    	Pageable pageable = PageRequest.of(0, pageSize);
    	Page<User> usrs = userRepository.findAll(pageable);
    	assertEquals(usrs.getSize(), pageSize);
    	
    	usrs.getContent().forEach(user->{log.info("■" + user.getName());});
    	assertEquals(usrs.getContent().get(1).getName(), "havi"+0);
    }
    
    @Test
    public void jpa_test3() {
    	int size = 10;
    	Pageable pageable = PageRequest.of(0, size, Sort.Direction.DESC, "idx");
    	Page<User> usrs = userRepository.findAll(pageable);
    	usrs.getContent().forEach(user->{log.info("■" + user.getName());});
    	
    	assertEquals(usrs.getSize(), size);
    	assertEquals(usrs.getContent().get(1).getName(), "havi"+0);
    }

}
