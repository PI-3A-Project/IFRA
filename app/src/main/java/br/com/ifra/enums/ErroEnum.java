package br.com.ifra.enums;

public enum ErroEnum {
    API("erro.conectar.api"), NO_BODY("erro.response.body"),
    RESPONSE("erro.response");

    private String value;

    public String getValue() {
        return value;
    }

    ErroEnum(String value) {
        this.value = value;
    }
}
