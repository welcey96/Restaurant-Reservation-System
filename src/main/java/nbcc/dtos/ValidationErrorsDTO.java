package nbcc.dtos;

import java.util.List;

public class ValidationErrorsDTO<T> {
    private List<ValidationErrorDTO> errors;
    private T entity;
    private int status;

    public ValidationErrorsDTO(T entity, List<ValidationErrorDTO> errors, int status) {
        this.entity = entity;
        this.errors = errors;
        this.status = status;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public List<ValidationErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationErrorDTO> errors) {
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
