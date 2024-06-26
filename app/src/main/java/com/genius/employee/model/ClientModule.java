package com.genius.employee.model;

public class ClientModule {
    String clientName,id;

    public ClientModule(String clientName, String id) {
        this.clientName = clientName;
        this.id=id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
