package br.com.ifra.data.model.dto.volume;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IsbnDTO {
    @SerializedName(value = "tipo", alternate = {"type"})
    @Expose
    private String tipo;
    @SerializedName(value = "numeroISBN", alternate = {"identifier"})
    @Expose
    private String numeroISBN;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumeroISBN() {
        return numeroISBN;
    }

    public void setNumeroISBN(String numeroISBN) {
        this.numeroISBN = numeroISBN;
    }
}
