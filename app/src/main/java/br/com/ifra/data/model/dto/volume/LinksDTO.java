package br.com.ifra.data.model.dto.volume;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import br.com.ifra.data.adapter.FlatteningAdapterFactory;

@JsonAdapter(FlatteningAdapterFactory.class)
public class LinksDTO {

    @SerializedName(value = "imagemLink", alternate = {"smallThumbnail"})
    @Expose
    private String imagemLink;

    public String getImagemLink() {
        return imagemLink;
    }

    public void setImagemLink(String imagemLink) {
        this.imagemLink = imagemLink;
    }
}
