package br.com.ifra.exceptions;

import br.com.ifra.enums.ErroEnum;
import br.com.ifra.utils.MessageUtil;

public class HttpMethodException extends Exception {
    private ErroEnum tipoErro;

    // Construtor sem argumentos
    public HttpMethodException() {
        super();
    }

    // Construtor que aceita uma mensagem
    public HttpMethodException(String message) {
        super(message);
    }

    public HttpMethodException(ErroEnum tipoErro) {
        super(MessageUtil.getMessage(tipoErro.getValue()));
        this.tipoErro = tipoErro;
    }

    public HttpMethodException(ErroEnum tipoErro, String message) {
        super(message);
        this.tipoErro = tipoErro;
    }

    // Construtor que aceita uma mensagem e uma causa
    public HttpMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    // Construtor que aceita uma causa
    public HttpMethodException(Throwable cause) {
        super(cause);
    }

    public ErroEnum getTipoErro() {
        return tipoErro;
    }

    public void setTipoErro(ErroEnum tipoErro) {
        this.tipoErro = tipoErro;
    }
}
