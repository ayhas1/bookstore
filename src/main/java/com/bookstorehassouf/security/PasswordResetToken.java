/* the reset token class to get a new password if forgotten
* The expiration time is set to be 12h*/
package com.bookstorehassouf.security;

import com.bookstorehassouf.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {

    private static final int EXPIRATION = 60*12;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;

    public PasswordResetToken(final String token, final User user) {
        super();
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private Date calculateExpiryDate(final int expiryTimeInMinutes){
        final Calendar cal= Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);


        return new Date(cal.getTime().getTime());
    }

    public void updateToken(final String token){
        this.token=token;
        this.expiryDate=calculateExpiryDate(EXPIRATION);
    }
}
