package matrixHub;

import arc.Events;
import arc.util.*;

import matrixHub.utils.*;

import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.mod.Plugin;
import mindustry.gen.Player;

import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Plugin {

    public Main() {
        final String servertitle = "WELCOME TO [red]A[yellow]L[teal]E[blue]X [gold]HUB";
        Config.main();
        //AtomicInteger time = new AtomicInteger();
        Events.on(ServerLoadEvent.class, event -> {
            Vars.netServer.admins.addActionFilter(playerAction -> false);

            Timer.schedule(() -> {
                for (int i = 0; i < Groups.player.size(); i++) {
                    Player p = Groups.player.index(i);
                    checkAndConnect(p);
                }
            }, 0.5f, 0.5f);
        });

        Events.on(PlayerJoin.class, event -> {
            //Call.label(event.player.con, ConfigTranslate.get("server4.title"), 1100f, 508f, 304f);
            Call.label(servertitle, 1100f, 200f, 220f);
            Call.label("PVP", 1100f, 350f, 350f);
            Call.label("SANDBOX", 1100f, 50f, 50f);
            Call.label("(but its actually PVP)", 1100f, 50f, 40f);
            Call.label("ATTACK", 1100f, 50f, 350f);
            Call.label("(but its actually PVP)", 1100f, 50f, 340f);
            Call.label("SURVIVAL", 1100f, 350f, 50f);
        });

    }

    public void checkAndConnect(Player player) {
        if (player.x <= 100f && player.x >= 10f && player.y >= 10f && player.y <= 100f) { // dont do exactly (0,0)
            Vars.net.pingHost("alexmindustry.ddns.net", 6568, host -> {
                Call.connect(player.con, "alexmindustry.ddns.net", 6568);
            }, e -> {
                showServerDownMessage(player);
            });
        }
        //if(true)//Boolean.parseBoolean(Config.get("server2Works")))
        if (player.x <= 100f && player.x >= 0f && player.y >= 300f && player.y <= 400f) {
            Vars.net.pingHost("alexmindustry.ddns.net", 6568, host -> {
                Call.connect(player.con, "alexmindustry.ddns.net", 6568);
            }, e -> {
                showServerDownMessage(player);
            });
        }
        if (player.x <= 400f && player.x >= 300f && player.y >= 300f && player.y <= 400f) { //upper right
            Vars.net.pingHost("alexmindustry.ddns.net", 6568, host -> {
                Call.connect(player.con, "alexmindustry.ddns.net", 6568);
            }, e -> {
                showServerDownMessage(player);
            });
        }
        if (player.x <= 400f && player.x >= 300f && player.y >= 0f && player.y <= 100f) {
            Vars.net.pingHost("alexmindustry.ddns.net", 6569, host -> {
                Call.connect(player.con, "alexmindustry.ddns.net", 6569);
            }, e -> {
                showServerDownMessage(player);
            });
        }
    }

    public void showServerDownMessage(Player p) {
        Call.label(p.con, "THIS SERVER IS DOWN ;-; PLEASE TRY AGAIN LATER", 5f, p.x, p.y + 15f);
    }

    @Override
    public void registerServerCommands(CommandHandler handler) {
        handler.register("ping", "Return \"Pong!\"", args -> {
            Log.info("Pong!");
        });
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        handler.<Player>register("reply25", "<text...>", "A simple ping command that echoes a player's text.", (args, player) -> {
            player.sendMessage("You said: [accent] " + args[0]);
        });
        //register a simple reply command
        handler.<Player>register("survival", "<text...>", "command to jump to survival.", (args, player) -> {
            player.sendMessage("You said: [accent] " + args[0]);
            Call.connect(player.con, "alexmindustry.ddns.net", 6569);
        });
        handler.<Player>register("pvp", "<text...>", "command to jump to survival.", (args, player) -> {
            player.sendMessage("You said: [accent] " + args[0]);
            Call.connect(player.con, "http://alexmindustry.ddns.net", 6568);
        });
    }

}

