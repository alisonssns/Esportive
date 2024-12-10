package model;

import java.util.ArrayList;

public class AdministradorController{
    private String nome;
    private String email;
    private String senha;
    private ArrayList<Integer> telefones;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public ArrayList<Integer> getTelefones() {
        return telefones;
    }

    public void setTelefones(ArrayList<Integer> telefones) {
        this.telefones = telefones;
    }

    public AdministradorController(String nome, String email, String senha, ArrayList<Integer> telefones) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefones = telefones;
    }
    
}
