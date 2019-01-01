package com.web.multifactor.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.web.multifactor.model.SampleModel;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SampleServiceTest {
	
	@Autowired
	SampleService sampleService;
	
	@Test
	public void getOneById() {
		SampleModel vo = sampleService.getOneById(1L);
		log.info(vo.toString());
		log.info("■■■■■■■■■■■■■");
	}
	
//	@Test
//	public void getAll(){
//		List<SampleModel> list = sampleService.getAll();
//		log.info("list : {}", list);
//	}
}
