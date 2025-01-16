package net.tv.twitch.chrono_fish.hit_and_brow.instance;

import org.bukkit.Material;

import java.util.ArrayList;

public enum GameColor {

    BLACK("Black","§0■",Material.BLACK_WOOL),
    WHITE("White","§f■",Material.WHITE_WOOL),
    RED("Red","§c■",Material.RED_WOOL),
    BLUE("Blue","§b■",Material.LIGHT_BLUE_WOOL),
    GREEN("Green","§a■",Material.LIME_WOOL),
    PINK("Pink","§d■",Material.PINK_WOOL),
    YELLOW("Yellow","§e■",Material.YELLOW_WOOL);

    private final String name;
    private final String colorBlock;
    private final Material material;

    GameColor(String name, String colorBlock, Material material){
        this.name = name;
        this.colorBlock = colorBlock;
        this.material = material;
    }

    public String getName() {return name;}
    public String getColorBlock() {return colorBlock;}
    public Material getMaterial() {return material;}

    public static ArrayList<Material> getMaterials(){
        ArrayList<Material> materials = new ArrayList<>();
        for(GameColor gameColor : GameColor.values()){
            materials.add(gameColor.getMaterial());
        }
        return materials;
    }

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
