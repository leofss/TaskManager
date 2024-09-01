package com.leonardo.taskmanager.repository.projection;

import com.leonardo.taskmanager.entity.User;

public interface UserProjection {
    String getUsername();
    String getEmail();
    User.Role getRole();
}
