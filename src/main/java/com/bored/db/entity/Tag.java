package com.bored.db.entity;

import com.bored.db.Column;
import lombok.Data;

@Data
public class Tag {
    @Column("ID")
    private Integer id;
    @Column("NAME")
    private String name;
}
