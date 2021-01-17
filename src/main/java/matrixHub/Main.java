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
import mindustry.net.Administration;
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
    final float gc = 8f;// tile xy = in game coords*8
    public Main() {

        servs.put("SANDBOX",new serverData("SANDBOX","alexmindustryattac.ddns.net",25814,gc*104,gc*104));
        servs.put("TURBO PVP",new serverData("TURBO PVP","alexmindustryturbo.ddns.net",25854,gc*121,gc*74));
        servs.put("TEST",new serverData("TEST","alexmindustryattac.ddns.net",25732,gc*74,gc*26));
        servs.put("HEX",new serverData("HEX","alexmindustryhex.ddns.net",25587,gc*74,gc*121));
        servs.put("ATTACK",new serverData("ATTACK","alexmindustryattac.ddns.net",25800,gc*44,gc*105));
        servs.put("PVP",new serverData("PVP","alexmindustry.ddns.net",6568,350f,350f));
        servs.put("SURVIVAL",new serverData("SURVIVAL","alexmindustry.ddns.net",6569,gc*27,gc*74));
        servs.put("secret test server",new serverData("secret test server","alexmindustrysecrettest.ddns.net",25590,gc*111,gc*146));

        final String servertitle = "WELCOME TO [red]A[yellow]L[teal]E[blue]X [gold]HUB";
        Config.main();
        Events.on(ServerLoadEvent.class, event -> {
            state.rules.modeName="HUB"; //still cant change this name ;-;
            Vars.netServer.admins.addActionFilter(playerAction -> {
                if (playerAction.type.equals(Administration.ActionType.configure)){
                    Call.sendMessage("pls dont configure things");
                }
                return false;//playerAction.player.admin;
            });
            Timer.schedule(() -> {
                for (int i = 0; i < Groups.player.size(); i++) {
                    Player p = Groups.player.index(i);
                    checkAndConnect(p);
                }
            }, 0.5f, 0.5f);
            Timer.schedule(() -> {
                updatePlayers();
            }, 5f, 30f);
        });

        Events.on(PlayerJoin.class, event -> {
            Call.label(servertitle, 1100f, gc*74, gc*88);
            servs.forEach( (ele)-> Call.label(ele.value.name, 1100f, ele.value.x, ele.value.y));
            updatePlayers();
            Timer.schedule(() -> {
                Call.label(event.player.con,"Know how to make plugins?? [red]A[yellow]L[teal]E[blue]X[white] wants [accent]YOU[]!",20f,event.player.x,event.player.y-20f);
            }, 6f );
        });

    }
    public void updatePlayers(){
        totalPlayers =0;
        for (ObjectMap.Entry<String,serverData> ee : servs.entries()){
            serverData sd = ee.value;
            Vars.net.pingHost(sd.address,sd.port,(v)->{
                totalPlayers =totalPlayers +v.players;
                String add_s = v.players<=1? "" :"s";
                Call.label(v.players +" player"+add_s+" inside", 29.9f, sd.x, sd.y-12f);
                Call.label(v.mapname, 29.9f, sd.x, sd.y-24f);
                Call.label(v.description, 29.9f, sd.x, sd.y-36f);
                Core.settings.put("totalPlayers", totalPlayers);
            },(f)->{
                Call.label("Server [red]OFFLINE[]", 29.9f, sd.x, sd.y-12f);
                //Call.label("Please [red]ping[] admins", 29.9f, sd.x, sd.y-24f);
            });
        }
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
        handler.<Player>register("reply22", "<#ID> <text...>", "A simple ping command that echoes a player's text.", (args, player) -> {
            player.sendMessage("You said: [accent] " + args[0]);
            player.sendMessage("and then : [accent] " + args[1]);
        });
        //register a simple reply command
        handler.<Player>register("showlocation",  "[scarlet](admin)[]Command to show location, shows up to 10secs.", (args, player) -> {
            final Player p = player;
            Timer.schedule(() -> {
                p.sendMessage("you are at x="+p.x+" y="+p.y);
            }, 1f,1f,10 );
        });
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

