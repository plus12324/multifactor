package com.web.multifactor.oauth;

/**
 * 소셜인증 타입정보
 * @since 2018.12.29
 */
public enum SocialType {
	KAKAO("kakao")
	, FACEBOOK("facebook"), GOOGLE("google"), ;

	private final String ROLE_PREFIX = "ROLE_";
	private String name;

	private SocialType(String name) {
		this.name = name;
	}

	public String getRoleType() {
		return ROLE_PREFIX + this.name.toUpperCase();
	}

	public String getValue() {
		return this.name;
	}
	
	public boolean isEquals(String authority) {
		return this.name.equals(authority);
	}
}
