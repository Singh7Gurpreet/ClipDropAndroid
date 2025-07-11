package helper;

public enum UPLOAD_DOWNLOAD {
    UPLOAD("Upload"),
    DOWNLOAD("Download");

    private final String value;

    UPLOAD_DOWNLOAD(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
