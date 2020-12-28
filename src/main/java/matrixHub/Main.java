package matrixHub;

import arc.Core;
import arc.Events;
import arc.struct.ObjectMap;
import arc.util.*;

import matrixHub.utils.*;

import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.game.EventType.*;
import mindustry.game.Gamemode;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.mod.Plugin;
import mindustry.gen.Player;
import mindustry.net.Host;

import java.nio.ByteBuffer;

import static mindustry.Vars.state;

class serverData{
    public String name;
    public String address;
    public int port;
    public float x,y;
    public serverData(String name,String address,int port,float x,float y){
        this.name =name;
        this.address =address;
        this.port = port;
        this.x = x;
        this.y= y;
    }
}
public class Main extends Plugin {
    public int totalPlayers=0;
    public ObjectMap<String,serverData> servs = new ObjectMap<>();

    public Main() {
        servs.put("SANDBOX",new serverData("SANDBOX","alexmindustryattac.ddns.net",25814,50f,50f));
        servs.put("TEST",new serverData("TEST","alexmindustryattac.ddns.net",25854,200f,50f));
        servs.put("ATTACK",new serverData("ATTACK","alexmindustryattac.ddns.net",25800,50f,350f));
        servs.put("PVP",new serverData("PVP","alexmindustry.ddns.net",6568,350f,350f));
        servs.put("SURVIVAL",new serverData("SURVIVAL","alexmindustry.ddns.net",6569,350f,50f));

        final String servertitle = "WELCOME TO [red]A[yellow]L[teal]E[blue]X [gold]HUB";
        Config.main();
        Events.on(ServerLoadEvent.class, event -> {
            state.rules.modeName="HUB"; //still cant change this name ;-;
            Vars.netServer.admins.addActionFilter(playerAction -> false);
            Timer.schedule(() -> {
                for (int i = 0; i < Groups.player.size(); i++) {
                    Player p = Groups.player.index(i);
                    checkAndConnect(p);
                }
            }, 0.5f, 0.5f);
            Timer.schedule(() -> {
                totalPlayers =0;
                for (ObjectMap.Entry<String,serverData> ee : servs.entries()){
                    serverData sd = ee.value;
                    Vars.net.pingHost(sd.address,sd.port,(v)->{
                        totalPlayers =totalPlayers +v.players;
                        String add_s = v.players<=1? "" :"s";
                        Call.label(v.players +" player"+add_s+" inside", 29.9f, sd.x, sd.y-12f);
                        Core.settings.put("totalPlayers", totalPlayers);
                    },(f)->{
                        Call.label("Server [red]OFFLINE[]", 29.9f, sd.x, sd.y-12f);
                        Call.label("Please [red]ping[] admins", 29.9f, sd.x, sd.y-24f);
                    });
                }
            }, 5f, 30f);
        });

        Events.on(PlayerJoin.class, event -> {
            Call.label(servertitle, 1100f, 200f, 220f);
            servs.forEach( (ele)-> Call.label(ele.value.name, 1100f, ele.value.x, ele.value.y));
        });

    }

    public void checkAndConnect(Player p) {
        servs.forEach(ele->{
            serverData sd = ele.value;
            if ( (p.x< (sd.x+24f)) && (p.x> (sd.x-24f)) && (p.y< (sd.y+24f)) && (p.y> (sd.y-24f)) ){
                Vars.net.pingHost(sd.address, sd.port, host -> {
                    Call.connect(p.con, sd.address, sd.port);
                }, e -> {
                    showServerDownMessage(p);
                });
            }
        });

    }

    public void showServerDownMessage(Player p) {
        Call.label(p.con, "THIS SERVER IS DOWN ;-; PLEASE TRY AGAIN LATER", 0.6f, p.x, p.y + 15f);
    }

    @Override
    public void registerServerCommands(CommandHandler handler) {
        handler.register("ping", "Return \"Pong!\"", args -> {
            Log.info("Pong!");
        });
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        handler.<Player>register("reply21", "<text...>", "A simple ping command that echoes a player's text.", (args, player) -> {
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

