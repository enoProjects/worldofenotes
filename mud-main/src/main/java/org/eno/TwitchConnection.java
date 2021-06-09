package org.eno;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.TwitchChatBuilder;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import org.eno.commands.*;
import org.eno.credentials.Credentials;
import org.eno.world.Player;
import org.eno.world.Space;
import org.eno.world.World;

import java.util.*;

public class TwitchConnection {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TwitchConnection.class);

    private OAuth2Credential oAuth2CredentialHere = new OAuth2Credential("twitch", Credentials.getInstance().getMainAuth());
    private OAuth2Credential botOAuth2CredentialHere = new OAuth2Credential("twitch", Credentials.getInstance().getBotAuth());

    private TwitchChat chatClient;

    private final World world = new World();

    private final Map<String, Player> players = new HashMap<>();

    private void makeBotChatConnection() {
        final CredentialManager credentialManager = CredentialManagerBuilder.builder().build();
        credentialManager.registerIdentityProvider(
                new TwitchIdentityProvider(
                        Credentials.getInstance().getClientId(),
                        Credentials.getInstance().getBotAuth(),
                        ""));

        chatClient = TwitchChatBuilder.builder()
                .withCredentialManager(credentialManager)
                .withChatAccount(botOAuth2CredentialHere)
                .build();


        chatClient.getEventManager().onEvent(PrivateMessageEvent.class,
                privateMessageEvent -> onPM(privateMessageEvent));
    }

    private void onPM(PrivateMessageEvent privateMessageEvent) {
//        log.info("Got pm " + privateMessageEvent.getUser());
        final String playerName = privateMessageEvent.getUser().getName();

        if (!players.containsKey(playerName)) {
            final Player newPlayer = new Player(playerName, world.getSpawn());
            players.put(playerName, newPlayer);
            onPlayerAppears(newPlayer);
            LOGGER.debug("Added player " + playerName);
        }

        try {
            LOGGER.debug("Command is " + privateMessageEvent.getMessage());
            final Optional<Command> optionalCommand = Commands.formCommand(privateMessageEvent.getMessage());


            if (!optionalCommand.isPresent()) {
                LOGGER.debug(playerName + ": Unknown command. -> " + privateMessageEvent.getMessage());
                return;
            } else {
                final Player player = players.get(playerName);
                LOGGER.debug("Using player " + playerName + " " + player);
                LOGGER.debug("Using player " + playerName + " command" + privateMessageEvent.getMessage());
                final Command command = optionalCommand.get();

                if (command instanceof LookCommand) {
                    look(player);
                } else if (command instanceof WalkCommand) {
                    walk(player, (WalkCommand) command);
                } else if (command instanceof SayCommand) {
                    LOGGER.debug("Trying to say " + ((SayCommand) command).getMessage());
                    say(player, (SayCommand) command);
                }

            }


        } catch (InvalidCommandException e) {
            chatClient.sendPrivateMessage(privateMessageEvent.getUser().getName(),
                    e.getReportForTheUser());
        }


    }

    private void say(final Player player,
                     final SayCommand command) {
        for (final String playerName : players.keySet()) {
            if (!Objects.equals(playerName, player.getName())) {
                if (Objects.equals(players.get(playerName).getCoordinate(), player.getCoordinate())) {
                    chatClient.sendPrivateMessage(playerName,
                            player.getName() + "Says: " + command.getMessage());
                }
            }
        }
    }

    private void walk(Player player, WalkCommand command) {
        boolean playerMoved = false;
        switch (command.getTarget()) {
            case "north":
//                if (World.WORLD_HEIGHT >= player.getCoordinate().getY() + 1) {
//                    //TODO Can not move out of map
//                }
                final int newYNorth = player.getCoordinate().getY() + 1;
                if (newYNorth < World.WORLD_HEIGHT) {
                    player.getCoordinate().setY(newYNorth);
                    playerMoved = true;
                } else {
                    messageCanNotGoThere(player);
                }
                break;
            case "south":

                int newYSouth = player.getCoordinate().getY() - 1;

                if (newYSouth >= 0) {
                    player.getCoordinate().setY(newYSouth);
                    playerMoved = true;
                } else {
                    messageCanNotGoThere(player);
                }
                break;
            case "west":
                int newXWest = player.getCoordinate().getX() - 1;
                if (newXWest >= 0) {
                    player.getCoordinate().setX(newXWest);
                    playerMoved = true;
                } else {
                    messageCanNotGoThere(player);
                }
                break;
            case "east":
                int newXEast = player.getCoordinate().getX() + 1;
                if (newXEast < World.WORLD_WIDTH) {
                    player.getCoordinate().setX(newXEast);
                    playerMoved = true;
                } else {
                    messageCanNotGoThere(player);
                }
                break;
            default:
                chatClient.sendPrivateMessage(player.getName(),
                        "I don't know where to walk");
        }

        if (playerMoved) {
            messageWalkedTo(player, command);
            onPlayerAppears(player);
        }

        look(player);
    }

    private void messageWalkedTo(Player player, WalkCommand command) {
        chatClient.sendPrivateMessage(player.getName(),
                "You walk to " + command.getTarget());
    }

    private void messageCanNotGoThere(Player player) {
        chatClient.sendPrivateMessage(player.getName(),
                "Can not go there");
    }

    private void look(Player player) {
        final Space space = world.getSpace(player.getCoordinate());
        final String message = space.getDescription();

        final List<String> playerNamesInTheCoordinate = new ArrayList<>();

        for (final String playerName : players.keySet()) {
            if (!Objects.equals(playerName, player.getName())) {
                if (Objects.equals(players.get(playerName).getCoordinate(), player.getCoordinate())) {
                    playerNamesInTheCoordinate.add(playerName);
                }
            }
            LOGGER.debug("Player " + playerName + " : " + players.get(playerName));
        }
        if (!playerNamesInTheCoordinate.isEmpty()) {

            final StringBuilder others = new StringBuilder();
            others.append("You see other players: ");
            others.append(String.join(", ", playerNamesInTheCoordinate));
            others.append(".");

            chatClient.sendPrivateMessage(player.getName(),
                    others.toString());
        }

        chatClient.sendPrivateMessage(player.getName(),
                message);
    }

    // TODO make HELP command
    // TODO maybe a way to join without appearing right away
    // TODO make a way to leave the game
    // TODO moderator tools, ban users and so on

    private void onPlayerAppears(final Player player) {
        // TODO player left

        for (final String playerName : players.keySet()) {
            if (!Objects.equals(playerName, player.getName())) {
                if (Objects.equals(players.get(playerName).getCoordinate(), player.getCoordinate())) {
                    chatClient.sendPrivateMessage(playerName,
                            "Player appears: " + player.getName());
                    // TODO Tell from where
                }
            }
        }
    }

    public void run() {

        final CredentialManager credentialManager = CredentialManagerBuilder.builder().build();
        credentialManager.registerIdentityProvider(new TwitchIdentityProvider(Credentials.getInstance().getClientId(), Credentials.getInstance().getMainAuth(), ""));

        makeBotChatConnection();
    }
}
