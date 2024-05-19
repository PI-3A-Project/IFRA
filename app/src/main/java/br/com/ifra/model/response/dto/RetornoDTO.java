package br.com.ifra.model.response.dto;

import br.com.ifra.model.erro.Error;

public class RetornoDTO<T extends Object> {
    private T retorno;
    private Error erro;
    private boolean sucesso;

    public T getRetorno() {
        return retorno;
    }

    public void setRetorno(T retorno) {
        this.retorno = retorno;
    }

    public Error getErro() {
        return erro;
    }

    public void setErro(Error erro) {
        this.erro = erro;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }
}
