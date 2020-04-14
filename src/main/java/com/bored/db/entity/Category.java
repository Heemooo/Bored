package com.bored.db.entity;

import com.bored.db.Column;
import lombok.Data;

@Data
public class Category {
    @Column("ID")
    private Integer id;
    @Column("NAME")
    private String name;
    @Column("COUNT")
    private String count;
    private String url;

}
