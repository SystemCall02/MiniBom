package com.idme.minibom.Request;

import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class BOMLinkRequest {
    // Getters and Setters for source and target
    private Source source;
    private Target target;


    // Nested static classes for Source and Target
    @Setter
    @Getter
    public static class Source {
        private Long id;

        @Override
        public String toString() {
            return "{\"id\":" + id + "}";
        }
    }

    @Setter
    @Getter
    public static class Target {
        private Long id;

        @Override
        public String toString() {
            return "{\"id\":" + id + "}";
        }
    }

    @Override
    public String toString() {
        return "{\"params\":{" +
                "\"source\":" + source +
                ", \"target\":" + target +
                "}}";
    }
}
