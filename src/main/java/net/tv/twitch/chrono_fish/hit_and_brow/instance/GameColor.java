package net.tv.twitch.chrono_fish.hit_and_brow.instance;

import org.bukkit.Material;

public enum GameColor {

    BLACK("Black","§0■",Material.BLACK_WOOL),
    WHITE("White","§f■",Material.WHITE_WOOL),
    RED("Red","§c■",Material.RED_WOOL),
    BLUE("Blue","§b■",Material.LIGHT_BLUE_WOOL),
    GREEN("Green","§a■",Material.LIME_WOOL),
    PINK("Pink","§d■",Material.PINK_WOOL),
    YELLOW("Yellow","§e■",Material.YELLOW_WOOL);

    private final String colorBlock;
    private final Material material;

    GameColor(String name, String colorBlock, Material material){
        this.colorBlock = colorBlock;
        this.material = material;
    }

    public String getColorBlockStr() {return colorBlock;}
    public Material getMaterial() {return material;}

    public static GameColor getHabColor(Material material){
        GameColor target = GameColor.BLACK;
        for(GameColor gameColor : GameColor.values()){
            if(material.equals(gameColor.getMaterial())){
                target = gameColor;
                break;
            }
        }
        return target;
    }

}
