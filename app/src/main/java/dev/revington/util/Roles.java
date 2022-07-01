package dev.revington.util;

public enum Roles {

    ADMIN("admin"),
    USER("user");

    public final String label;

    private Roles(String label) {
        this.label = label;
    }

}
