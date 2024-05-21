package br.com.ifra.data.model.dto;

import br.com.ifra.data.model.dto.erro.ErrorDTO;

public class ReturnDTO<T extends Object> {
    private T retorno;
    private ErrorDTO erro;
    private boolean sucesso;

    public T getRetorno() {
        return retorno;
    }

    public void setRetorno(T retorno) {
        this.retorno = retorno;
    }

    public ErrorDTO getErro() {
        return erro;
    }

    public void setErro(ErrorDTO erro) {
        this.erro = erro;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }
}
