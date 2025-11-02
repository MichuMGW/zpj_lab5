package com.example.library.library.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reader {
    private int id;
    private String name;
    private String email;
    private boolean active = true;
}