package dev.revington.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.util.Date;

@Document(collection = "tokens")
public class Token {

    @Id
    private int id;
    private String token;
    private long expires;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getExpires() {
        return new Timestamp(expires);
    }

    public void setExpires(Timestamp expires) {
        this.expires = expires.getTime();
    }

}
