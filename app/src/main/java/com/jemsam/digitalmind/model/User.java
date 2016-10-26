package com.jemsam.digitalmind.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by jeremy.toussaint on 25/10/16.
 */

public class User extends SugarRecord {

    private String login;
    private String password;

    public User() {
    }

    public User(String login, String password) {

        this.login = login;
        this.password = password;
    }

    /**
     *
     * @return
     * The login
     */
    public String getLogin() {
        return login;
    }

    /**
     *
     * @param login
     * The login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public static User getUser(String login, String password){

        List<User> users = getAllUsers();

        if (users.size() > 0){
            for (User user: users){
                if (login.equals(user.getLogin()) && password.equals(user.getPassword())){
                    return user;
                }
            }
        }

        User user = new User(login, password);
        user.save();

        return user;
    }

    public static List<User> getAllUsers(){
        return User.listAll(User.class);
    }



}
