package com.bored.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Command {
    private String name;
    private List<String> targetParameter;
    private String description;
    private String allowAddTo;
    private CommandExecuter handler;
}
