package com.bored.db.entity;

import com.bored.db.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaticResource {

    @Column("URI")
    private String uri;

    @Column("FILE_PATH")
    private String filePath;

}
