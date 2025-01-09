package model;
import java.time.LocalTime;

public class LocalModel {
    private String nome;
    private String tipo;
    private int cep;
    private int numero;
    private int limiteDia;
    private LocalTime tempoMaximo;
    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCep() {
        return cep;
    }

    public void setCep(int cep) {
        this.cep = cep;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getLimiteDia() {
        return limiteDia;
    }

    public void setLimiteDia(int limiteDia) {
        this.limiteDia = limiteDia;
    }

    public LocalTime getTempoMaximo() {
        return tempoMaximo;
    }

    public void setTempoMaximo(LocalTime tempoMaximo) {
        this.tempoMaximo = tempoMaximo;
    }

    public LocalTime getHorarioAbertura() {
        return horarioAbertura;
    }

    public void setHorarioAbertura(LocalTime horarioAbertura) {
        this.horarioAbertura = horarioAbertura;
    }

    public LocalTime getHorarioFechamento() {
        return horarioFechamento;
    }

    public void setHorarioFechamento(LocalTime horarioFechamento) {
        this.horarioFechamento = horarioFechamento;
    }

}
