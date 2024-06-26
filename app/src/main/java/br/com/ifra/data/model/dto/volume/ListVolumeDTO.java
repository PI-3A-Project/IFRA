package br.com.ifra.data.model.dto.volume;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.com.ifra.base.BeanAbstrato;
import br.com.ifra.data.adapter.FlatteningAdapterFactory;

@JsonAdapter(FlatteningAdapterFactory.class)
public class ListVolumeDTO extends BeanAbstrato {
    @SerializedName(value = "listaVolumes", alternate = {"items"})
    @Expose
    private List<VolumeDTO> listaVolumes;

    public List<VolumeDTO> getListaVolumes() {
        return listaVolumes;
    }

    public void setListaVolumes(List<VolumeDTO> listaVolumes) {
        this.listaVolumes = listaVolumes;
    }
}
