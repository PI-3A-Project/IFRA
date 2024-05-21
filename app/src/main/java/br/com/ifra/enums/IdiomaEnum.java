package br.com.ifra.enums;

public enum IdiomaEnum {
    pt(1), en(2);

    private Integer value;

    public Integer getValue() {
        return value;
    }

    IdiomaEnum(Integer value) {
        this.value = value;
    }

    public static IdiomaEnum fromInt(Integer value) {
        for (IdiomaEnum e: IdiomaEnum.values()) {
            if(e.value.equals(value)) {
                return e;
            }
        }
        return null;
    };
}
