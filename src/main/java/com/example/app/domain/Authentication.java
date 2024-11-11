package com.example.app.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="authentications")
public class Authentication {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name="username",length = 200, nullable = false)
    private String Username; //AMKA Γιατρού ή Φαρμακοποιού

    @Column(name="password",length = 50, nullable = false)
    private String Password;

    public Authentication() {

    }

    public Authentication(String username, String password) {
        this.Username = username;
        this.Password = password;
    }

    public String getUsername() {
        //System.out.println("USERNAME = "+Username);
        return Username;
    }

    public String getPassword() {
        return Password;
    }


    /*Ένας χρήστης δεν θα μπορεί να δημιουργήσει λογαριασμό αν δεν βάλει και
    * username και password μαζι.*/
    public Authentication setUsernamePassword(String username, String password){
        Authentication user = new Authentication(username,password);
        return user;
    }


    /*Για να πετύχει η αλλαγή κωδικού θα πρέπει και το username και το pass να είναι σωστά*/
    public void changeLogInPassword(String username, String password, String new_password) throws DomainException{
        if(password == Password && username == Username){
            this.Password = new_password;
            //System.out.println("NEW PASSWORD = "+ new_password);
        }else{
            //System.out.println("Wrong old password!!!!!!!!!!!");
            throw new DomainException("Wrong old username or password");
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Authentication)) return false;
        Authentication that = (Authentication) o;
        return Username.equals(that.Username) && Password.equals(that.Password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Username, Password);
    }
}

