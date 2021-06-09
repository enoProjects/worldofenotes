package org.eno.commands;

import org.eno.InvalidCommandException;

import java.util.Optional;

public class Commands {

    private static final String SAY_COMMAND = "say";

    public static Optional<Command> formCommand(final String message)
            throws InvalidCommandException {

        final String[] split = message.split(" ");

        final String command = split[0];

        switch (command.toLowerCase()) {
            case "walk":
                if (split.length != 2) {
                    throw new InvalidCommandException("Walk where?");
                }

                return Optional.of(new WalkCommand(split[1]));

            case "look":

                return Optional.of(new LookCommand());
            case SAY_COMMAND:
                try {
                    return Optional.of(new SayCommand(message.substring(SAY_COMMAND.length() + 1)));
                } catch (Exception e) {
                    throw new InvalidCommandException("Say what?");
                }
        }
        return Optional.empty();

    }
}
