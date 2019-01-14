package com.web.multifactor.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.multifactor.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findOneByPincipal(String pincipal);
	User findOneByIdx(Long idx);
}
