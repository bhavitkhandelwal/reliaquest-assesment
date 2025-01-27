package com.reliaquest.api.model;

import lombok.Data;

@Data
public class DeleteMockEmployeeInput {
    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public DeleteMockEmployeeInput(String employeeName) {
    }


}