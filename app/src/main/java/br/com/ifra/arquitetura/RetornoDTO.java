package br.com.ifra.arquitetura;

import br.com.ifra.ws.bean.erro.ErroBean;

public class RetornoDTO<T extends Object> {
    private T retorno;
    private ErroBean erro;
    private boolean sucesso;

    public T getRetorno() {
        return retorno;
    }

    public void setRetorno(T retorno) {
        this.retorno = retorno;
    }

    public ErroBean getErro() {
        return erro;
    }

    public void setErro(ErroBean erro) {
        this.erro = erro;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }
}
