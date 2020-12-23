package matrixHub.utils;

import mindustry.Vars;
//import mindustry.content.Mechs;
//import mindustry.entities.type.Player;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
//import mindustry.type.Mech;


public class Upd {

    static Thread th;

    public static void main(Player player) {

        boolean ok = true;
        boolean first = true;

        th = new Thread(new Runnable() {
            public void run() {
                try {
                    while(ok) {
                        boolean oK = Chek(player, first);
                        Thread.sleep(100);
                        if (oK) {
                            chekTile(player);
                        } else th.stop();
                    }
                } catch (InterruptedException ignored) {
                    main(player);
                }
            }
        });
        th.start();
    }


    public static boolean Chek(Player player, boolean first) {
        boolean ok = false;

        if (first) {
            first=false;
            return true;
        }
        for(int id = 0; id < Groups.player.size(); id++){
            player.sendMessage("qqq");
            Player pl = Groups.player.index(id);
            if(pl == player) {
                ok = true;
            }
        }

        return ok;
    }

    public static void chekTile(Player player) {

                            if(true)//Boolean.parseBoolean(Config.get("server1Works")))
                                if (player.x <= 10 && player.x >= 0 && player.y >= 0 && player.y <= 10) {

                                    Vars.net.pingHost("http://alexmindustry.ddns.net", 6569, host -> {
                                        Call.connect(player.con, "http://alexmindustry.ddns.net", 6569);
                                    }, e -> {
                                    });
                                }

                            if(true)//Boolean.parseBoolean(Config.get("server2Works")))
                                if (player.x <= 10 && player.x >= 0 && player.y >= 40 && player.y <= 50) {
                                    Vars.net.pingHost("alexmindustry.ddns.net", 6568, host -> {
                                        Call.connect(player.con, "alexmindustry.ddns.net", 6568);
                                    }, e -> {
                                    });
                                }
//
//                            if(Boolean.parseBoolean(Config.get("server3Works")))
//                                if (player.x <= 306 && player.x >= 263 && player.y <= 306 && player.y >= 263) {
//
//                                    Vars.net.pingHost(Config.get("ip3"), Integer.parseInt(Config.get("port3")), host -> {
//                                        Call.connect(player.con, Config.get("ip3"), Integer.parseInt(Config.get("port3")));
//                                    }, e -> {
//                                    });
//                                }
//
//                            if(Boolean.parseBoolean(Config.get("server4Works")))
//                                if (player.x >= 487 && player.x <= 528 && player.y <= 306 && player.y >= 263) {
//                                    Vars.net.pingHost(Config.get("ip4"), Integer.parseInt(Config.get("port4")), host -> {
//                                        Call.connect(player.con, Config.get("ip4"), Integer.parseInt(Config.get("port4")));
//                                    }, e -> {
//                                    });
//                                }
                        }
}
