package com.oan.management.service.image;

import org.springframework.stereotype.Service;

@Service
public class StorageProperties {

    private String location = "upload";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
