package fioshi.com.github.safedriver.SafeDriver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverCreateDTO {

    private String nome;
    private String email;
    private String password;
    private String telefone;
    private String objetivoDeDirecao;
    private String emailCorporativo;

}
