package com.web.multifactor.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.web.multifactor.oauth.SocialType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 필듭명이 카멜표기법인경우 jpa에선 언더스코어(_)로 구분
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table
public class User implements Serializable {

	private static final long serialVersionUID = 7012740216021380252L;

	@Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String pincipal;

    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime updatedDate;

    @Transient 
    private LocalDate inputDate;
    
    public LocalDate getInputDate() {
    	inputDate = LocalDate.from(this.getCreatedDate());
		return inputDate;
	}



	@Builder
    public User(String name, String password, String email, String pincipal, SocialType socialType, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.pincipal = pincipal;
        this.socialType = socialType;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }   
}
