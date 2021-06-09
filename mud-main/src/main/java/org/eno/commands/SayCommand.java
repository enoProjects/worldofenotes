package org.eno.commands;

public class SayCommand extends Command {
    private final String message;

    public SayCommand(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
