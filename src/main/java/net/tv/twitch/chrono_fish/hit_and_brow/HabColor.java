package net.tv.twitch.chrono_fish.hit_and_brow;

import org.bukkit.Material;

import java.util.ArrayList;

public enum HabColor {

    BLACK("黒色","§0■",Material.BLACK_WOOL),
    WHITE("白色","§f■",Material.WHITE_WOOL),
    RED("赤色","§c■",Material.RED_WOOL),
    BLUE("青色","§b■",Material.LIGHT_BLUE_WOOL),
    GREEN("緑色","§a■",Material.LIME_WOOL),
    PINK("桃色","§d■",Material.PINK_WOOL),
    YELLOW("黄色","§e■",Material.YELLOW_WOOL);

    private final String name;
    private final String colorBlock;
    private final Material material;

    HabColor(String name, String colorBlock, Material material){
        this.name = name;
        this.colorBlock = colorBlock;
        this.material = material;
    }

    public String getName() {return name;}
    public String getColorBlock() {return colorBlock;}
    public Material getMaterial() {return material;}

    public static ArrayList<Material> getMaterials(){
        ArrayList<Material> materials = new ArrayList<>();
        for(HabColor habColor : HabColor.values()){
            materials.add(habColor.getMaterial());
        }
        return materials;
    }

    public static HabColor getHabColor(Material material){
        HabColor target = null;
        for(HabColor habColor: HabColor.values()){
            if(material.equals(habColor.getMaterial())){
                target = habColor;
                break;
            }
        }
        if(target == null) target = HabColor.BLACK;
        return target;
    }

}
