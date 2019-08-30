package com.admin.work.litepal;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * @author Lv
 * Created at 2019/6/22
 */
public class AccountTable{

    private String name;
    /**
     * 不能为空 且 必须是唯一的
     */
    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,unique = true)
    private String phone;

    private String password;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
