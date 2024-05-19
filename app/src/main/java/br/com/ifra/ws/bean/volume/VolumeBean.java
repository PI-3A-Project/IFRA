package br.com.ifra.ws.bean.volume;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.com.ifra.arquitetura.BeanAbstrato;
import br.com.ifra.ws.bean.volume.modelo.IsbnBean;

public class VolumeBean extends BeanAbstrato {
    @SerializedName(value = "titulo", alternate = {"title"})
    @Expose
    private String titulo;

    @SerializedName(value = "subTitulo", alternate = {"subtitle"})
    @Expose
    private String subTitulo;

    @SerializedName(value = "autores", alternate = {"authors"})
    @Expose
    private List<String> autores;

    @SerializedName(value = "descricao", alternate = {"description"})
    @Expose
    private String descricao;

    @SerializedName(value = "editora", alternate = {"publisher"})
    @Expose
    private String editora;

    @SerializedName(value = "dataPublicacao", alternate = {"publishedDate"})
    @Expose
    private String dataPublicacao;

    @SerializedName(value = "identificador", alternate = {"industryIdentifiers"})
    @Expose
    private List<IsbnBean> identificador;

    @SerializedName(value = "lingua", alternate = {"language"})
    @Expose
    private String lingua;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubTitulo() {
        return subTitulo;
    }

    public void setSubTitulo(String subTitulo) {
        this.subTitulo = subTitulo;
    }

    public List<String> getAutores() {
        return autores;
    }

    public void setAutores(List<String> autores) {
        this.autores = autores;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public List<IsbnBean> getIdentificador() {
        return identificador;
    }

    public void setIdentificador(List<IsbnBean> identificador) {
        this.identificador = identificador;
    }

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }
}
