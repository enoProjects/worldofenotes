package org.eno.commands;

import org.eno.commands.Command;

public class WalkCommand extends Command {

    private String target;

    public WalkCommand(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }
}
