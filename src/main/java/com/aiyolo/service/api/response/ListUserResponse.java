package com.aiyolo.service.api.response;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.service.api.request.RequestObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ListUserResponse extends Response {

    private List<AppUserObject> users;

    public ListUserResponse(RequestObject request, List<AppUserObject> users) {
        super(request.getAction(), users.size() == 0 ? ApiResponseStateEnum.ERROR_NONE_DATA : ApiResponseStateEnum.SUCCESS);

        this.users = users;
    }

    @Override
    public String toString() {
        return super.toString() + ", ListUserResponse{" +
                "users='" + users.toString() + '\'' +
                '}';
    }

    public List<AppUserObject> getUsers() {
        return users;
    }

    public void setUsers(List<AppUserObject> users) {
        this.users = users;
    }

}
