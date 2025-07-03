package helper;

public interface CustomCallback<T> {
    boolean onSuccess(T result);

    boolean onFailure(T errorMessage);
}