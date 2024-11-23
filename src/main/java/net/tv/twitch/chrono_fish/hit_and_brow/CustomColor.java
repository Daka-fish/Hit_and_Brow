package net.tv.twitch.chrono_fish.hit_and_brow;

import org.bukkit.Material;

import java.util.ArrayList;

public enum CustomColor {

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

    CustomColor(String name, String colorBlock, Material material){
        this.name = name;
        this.colorBlock = colorBlock;
        this.material = material;
    }

    public String getColorBlock() {return colorBlock;}
    public Material getMaterial() {return material;}

    public static ArrayList<Material> getMaterials(){
        ArrayList<Material> materials = new ArrayList<>();
        for(CustomColor customColor : CustomColor.values()){
            materials.add(customColor.getMaterial());
        }
        return materials;
    }

    public static CustomColor getHabColor(Material material){
        CustomColor target = CustomColor.BLACK;
        for(CustomColor customColor : CustomColor.values()){
            if(material.equals(customColor.getMaterial())){
                target = customColor;
                break;
            }
        }
        return target;
    }

}
