package com.product.main;

import com.product.dtos.User;
import com.product.util.JsonUtil;

import java.util.List;

public class MainExample {

    public static void main(String[] args) {
        convertUserToJson();
        convertJsonToUser();
        convertJsonToUserList();

    }

    private static void convertJsonToUserList() {
        String json = "[{\"name\":\"John\",\"age\":30},{\"name\":\"Mike\",\"age\":25}]";
        List<User> users = JsonUtil.fromJsonToList(json, User.class);
        System.out.println(users);
    }

    private static void convertJsonToUser() {
        String json = "{\"name\":\"John\",\"age\":30}";
        User user = JsonUtil.fromJson(json, User.class);
        System.out.println(user);
    }

    private static void convertUserToJson() {
        User user = new User("Hari",36);
        String json = JsonUtil.toJson(user);
        System.out.println(json);
        String pretty = JsonUtil.toPrettyJson(user);
        System.out.println(pretty);
    }
}
