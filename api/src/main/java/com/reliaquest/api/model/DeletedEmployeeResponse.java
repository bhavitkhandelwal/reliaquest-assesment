package com.reliaquest.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DeletedEmployeeResponse<T>(T data, Status status, String error) {

    public static <T> DeletedEmployeeResponse<T> handled() {
        return new DeletedEmployeeResponse<>(null, Status.HANDLED, null);
    }

    public static <T> DeletedEmployeeResponse<T> handledWith(T data) {
        return new DeletedEmployeeResponse<>(data, Status.HANDLED, null);
    }

    public static <T> DeletedEmployeeResponse<T> error(String error) {
        return new DeletedEmployeeResponse<>(null, Status.ERROR, error);
    }

    public enum Status {
        HANDLED("Successfully processed request."),
        ERROR("Failed to process request.");

        @JsonValue
        @Getter
        private final String value;

        Status(String value) {
            this.value = value;
        }
    }
}
