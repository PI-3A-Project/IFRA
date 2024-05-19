package br.com.ifra.ws.bean.erro;

import java.util.List;

import br.com.ifra.arquitetura.BeanAbstrato;
import br.com.ifra.arquitetura.enums.ErroEnum;

public class ErroBean extends BeanAbstrato {
    private ErroEnum tipoErro;
    private String mensagem;
    private List<ErroBean> subErros;

    private ErroBean(Builder builder) {
        this.tipoErro = builder.tipoErro;
        this.mensagem = builder.mensagem;
        this.subErros = builder.subErros;
    }

    public static class Builder {
        private ErroEnum tipoErro;
        private String mensagem;
        private List<ErroBean> subErros;

        public Builder() {
        }

        public Builder(ErroEnum tipoErro, String mensagem, List<ErroBean> subErros) {
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

        public Builder subErros(List<ErroBean> subErros) {
            this.subErros.addAll(subErros);
            return this;
        }

        public Builder erro(ErroBean subErro) {
            this.subErros.add(subErro);
            return this;
        }

        public ErroBean build() {
            return new ErroBean(this);
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

    public List<ErroBean> getSubErros() {
        return subErros;
    }

    public void setSubErros(List<ErroBean> subErros) {
        this.subErros = subErros;
    }
}
