package br.com.ifra.data.model.dto.volume;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.com.ifra.base.BeanAbstrato;
import br.com.ifra.data.adapter.FlatteningAdapterFactory;

@JsonAdapter(FlatteningAdapterFactory.class)
public class VolumeDTO extends BeanAbstrato {
    @SerializedName(value = "titulo", alternate = {"volumeInfo.title"})
    @Expose
    private String titulo;

    @SerializedName(value = "subTitulo", alternate = {"volumeInfo.subtitle"})
    @Expose
    private String subTitulo;

    @SerializedName(value = "autores", alternate = {"volumeInfo.authors"})
    @Expose
    private List<String> autores;

    @SerializedName(value = "editora", alternate = {"volumeInfo.publisher"})
    @Expose
    private String editora;

    @SerializedName(value = "dataPublicacao", alternate = {"volumeInfo.publishedDate"})
    @Expose
    private String dataPublicacao;

    @SerializedName(value = "descricao", alternate = {"volumeInfo.description"})
    @Expose
    private String descricao;

    @SerializedName(value = "identificador", alternate = {"volumeInfo.industryIdentifiers"})
    @Expose
    private List<IsbnDTO> identificador;

    @SerializedName(value = "lingua", alternate = {"volumeInfo.language"})
    @Expose
    private String lingua;

    @SerializedName(value = "links", alternate = {"volumeInfo.imageLinks"})
    @Expose
    private LinksDTO links;

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

    public List<IsbnDTO> getIdentificador() {
        return identificador;
    }

    public void setIdentificador(List<IsbnDTO> identificador) {
        this.identificador = identificador;
    }

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    public LinksDTO getLinks() {
        return links;
    }

    public void setLinks(LinksDTO links) {
        this.links = links;
    }
}
