package com.ajcp.service.user.model.repository;

import com.ajcp.service.common.user.model.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<UserModel, Long> {

    @RestResource(path = "find-username")
    public UserModel findByUsername(@Param("name") String username);

    @Query("select u from UserModel u where u.username=?1")
    public UserModel getPerUsername(String username);

}