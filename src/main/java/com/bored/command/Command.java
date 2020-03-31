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
    private boolean isStart = true;
    private boolean isEnd = false;
    private CommandHandler handler;
}
