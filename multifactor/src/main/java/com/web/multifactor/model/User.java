package com.web.multifactor.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.web.multifactor.oauth.SocialType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 필듭명이 카멜표기법인경우 jpa에선 언더스코어(_)로 구분
 */
@Getter
@NoArgsConstructor
@Entity
@Table
public class User implements Serializable {

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

    @Column(name="socialtype")
    @Enumerated(EnumType.STRING)
    private SocialType socialtype;

    @Column(name="createddate")
    private LocalDateTime createddate;

    @Column(name="updateddate")
    private LocalDateTime updateddate;

    @Builder
    public User(String name, String password, String email, String pincipal, SocialType socialtype, LocalDateTime createddate, LocalDateTime updateddate) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.pincipal = pincipal;
        this.socialtype = socialtype;
        this.createddate = createddate;
        this.updateddate = updateddate;
    }
}
