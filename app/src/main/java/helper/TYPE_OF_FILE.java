package helper;

public enum TYPE_OF_FILE {
    STORAGE("storage"),
    CLIPBOARD("clipboard");

    private final String value;

    TYPE_OF_FILE(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
