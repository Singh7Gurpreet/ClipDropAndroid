package helper;

public interface CustomCallback<T> {
    void onSuccess(T result);

    void onFailure(T errorMessage);
}