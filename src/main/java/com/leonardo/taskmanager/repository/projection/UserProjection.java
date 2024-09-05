package com.leonardo.taskmanager.repository.projection;

import com.leonardo.taskmanager.entity.User;

public interface UserProjection {
    Long getId();
    String getUsername();
    String getEmail();
    User.Role getRole();
}
