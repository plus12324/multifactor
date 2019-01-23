package com.web.multifactor.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.multifactor.model.SampleModel;

@Mapper
public interface SampleRepository {

	SampleModel selectOneById(Long id);
	List<SampleModel> selectAll();
	public void insert(SampleModel sampleModel);
}
