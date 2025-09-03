package fioshi.com.github.safedriver.SafeDriver.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DriverResponseDTO {

    private Integer id_motorista;
    private String nome;
    private String email;
    private int points;
    private LocalDateTime data_cadastro;
    private String outros_dados;
    private List<VehicleResponseDTO> vehicles;

    // Getters and Setters

    public Integer getId_motorista() {
        return id_motorista;
    }

    public void setId_motorista(Integer id_motorista) {
        this.id_motorista = id_motorista;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDateTime getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(LocalDateTime data_cadastro) {
        this.data_cadastro = data_cadastro;
    }

    public String getOutros_dados() {
        return outros_dados;
    }

    public void setOutros_dados(String outros_dados) {
        this.outros_dados = outros_dados;
    }

    public List<VehicleResponseDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleResponseDTO> vehicles) {
        this.vehicles = vehicles;
    }
}
