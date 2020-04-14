package com.bored.db.entity;

import com.bored.db.Column;
import com.bored.db.Table;
import lombok.Data;

@Data
@Table("TAG")
public class Tag {
    @Column("ID")
    private Integer id;
    @Column("NAME")
    private String name;
    @Column("COUNT")
    private String count;
}
