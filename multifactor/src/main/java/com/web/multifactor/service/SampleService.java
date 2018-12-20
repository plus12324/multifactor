package com.web.multifactor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.multifactor.model.SampleModel;
import com.web.multifactor.repository.SampleRepository;

@Service
@Transactional
public class SampleService {

	@Autowired
	SampleRepository sampleRepository;
	
	public SampleModel getOneById(Long id) {
		return sampleRepository.selectOneById(id);
	}
	
	public List<SampleModel> getAll(){
		return sampleRepository.selectAll();
	}
}
