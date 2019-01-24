package com.web.multifactor.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Setter
@Getter
public class SampleModel {
	private Long id;
	private String name;	
	
	@Builder
	public SampleModel(Long id, String name) {
		this.id = id;
		this.name = name;
	}
}
