package br.com.ifra.data.model.dto.volume;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import br.com.ifra.data.adapter.FlatteningAdapterFactory;

@JsonAdapter(FlatteningAdapterFactory.class)
public class LinksDTO {

    @SerializedName(value = "urlCapaPequena", alternate = {"smallThumbnail"})
    @Expose
    private String urlCapaPequena;

    @SerializedName(value = "urlCapaNormal", alternate = {"thumbnail"})
    @Expose
    private String urlCapaNormal;

    public String getUrlCapaPequena() {
        return urlCapaPequena;
    }

    public void setUrlCapaPequena(String urlCapaPequena) {
        this.urlCapaPequena = urlCapaPequena;
    }

    public String getUrlCapaNormal() {
        return urlCapaNormal;
    }

    public void setUrlCapaNormal(String urlCapaNormal) {
        this.urlCapaNormal = urlCapaNormal;
    }
}
