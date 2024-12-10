package model;

import java.sql.Date;
import java.sql.Time;

public class ReservaModel {
    private Date data;
    private Time horario;
    private int idLocal;
    private String status;

    public int getIdLocal() {
        return idLocal;
    }
    
    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Time getHorario() {
        return horario;
    }

    public void setHorario(Time horario) {
        this.horario = horario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}