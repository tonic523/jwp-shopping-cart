package woowacourse.shoppingcart.dto;

import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class CustomerRequest {
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")
    private String password;

    @Pattern(regexp = "^[가-힣A-Za-z0-9]{2,}$")
    private String nickname;

    public CustomerRequest() {
    }

    public CustomerRequest(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }
}
