package br.com.ifra.model.erro;

import java.util.List;

import br.com.ifra.abstratos.BeanAbstrato;
import br.com.ifra.enums.ErroEnum;

public class Error extends BeanAbstrato {
    private ErroEnum tipoErro;
    private String mensagem;
    private List<Error> subErros;

    private Error(Builder builder) {
        this.tipoErro = builder.tipoErro;
        this.mensagem = builder.mensagem;
        this.subErros = builder.subErros;
    }

    public static class Builder {
        private ErroEnum tipoErro;
        private String mensagem;
        private List<Error> subErros;

        public Builder() {
        }

        public Builder(ErroEnum tipoErro, String mensagem, List<Error> subErros) {
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

        public Builder subErros(List<Error> subErros) {
            this.subErros.addAll(subErros);
            return this;
        }

        public Builder erro(Error subErro) {
            this.subErros.add(subErro);
            return this;
        }

        public Error build() {
            return new Error(this);
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

    public List<Error> getSubErros() {
        return subErros;
    }

    public void setSubErros(List<Error> subErros) {
        this.subErros = subErros;
    }
}
