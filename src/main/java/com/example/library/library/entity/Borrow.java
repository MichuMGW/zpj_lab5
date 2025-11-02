package com.example.library.library.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Borrow {
    private int id;
    private Book book;
    private Reader reader;
    private LocalDate date;

}