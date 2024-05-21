package br.com.ifra.data.model.dto.erro;

import java.util.List;

import br.com.ifra.base.BeanAbstrato;
import br.com.ifra.enums.ErroEnum;

public class ErrorDTO extends BeanAbstrato {
    private ErroEnum tipoErro;
    private String mensagem;
    private List<ErrorDTO> subErros;

    private ErrorDTO(Builder builder) {
        this.tipoErro = builder.tipoErro;
        this.mensagem = builder.mensagem;
        this.subErros = builder.subErros;
    }

    public static class Builder {
        private ErroEnum tipoErro;
        private String mensagem;
        private List<ErrorDTO> subErros;

        public Builder() {
        }

        public Builder(ErroEnum tipoErro, String mensagem, List<ErrorDTO> subErros) {
            this.tipoErro = tipoErro;
            this.mensagem = mensagem;
            this.subErros = subErros;
        }

        public Builder tipoErro(ErroEnum txErro) {
            this.tipoErro = txErro;
            return this;
        }

        public Builder mensagem(String mensagem) {
            this.mensagem = mensagem;
            return this;
        }

        public Builder subErros(List<ErrorDTO> subErros) {
            this.subErros.addAll(subErros);
            return this;
        }

        public Builder subErro(ErrorDTO subErro) {
            this.subErros.add(subErro);
            return this;
        }

        public ErrorDTO build() {
            return new ErrorDTO(this);
        }
    }

    public ErroEnum getTipoErro() {
        return tipoErro;
    }

    public void setTipoErro(ErroEnum tipoErro) {
        this.tipoErro = tipoErro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<ErrorDTO> getSubErros() {
        return subErros;
    }

    public void setSubErros(List<ErrorDTO> subErros) {
        this.subErros = subErros;
    }
}
