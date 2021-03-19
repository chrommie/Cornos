/*
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
# Project: Cornos
# File: Hologram
# Created by constantin at 14:38, Mär 18 2021
PLEASE READ THE COPYRIGHT NOTICE IN THE PROJECT ROOT, IF EXISTENT
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
*/
package me.constantindev.ccl.command;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import me.constantindev.ccl.Cornos;
import me.constantindev.ccl.etc.base.Command;
import me.constantindev.ccl.etc.helper.ClientHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Hologram extends Command {
    public Hologram() {
        super("Hologram","Creates a hologram where you stand",new String[]{"hologram","holo","hg","hlg"});
    }

    @Override
    public void onExecute(String[] args) {
        OptionParser optionParser = new OptionParser();
        optionParser.allowsUnrecognizedOptions();
        optionParser.accepts("force");
        optionParser.accepts("spawnEgg");
        optionParser.accepts("baby");
        OptionSet oset = optionParser.parse(args);
        if (!Cornos.minecraft.player.isCreative() && !oset.has("force")) {
            ClientHelper.sendChat("You need to be in creative (or provide --force)");
            return;
        }
        if (args.length < 1) {
            ClientHelper.sendChat("Syntax ([required], <optional>): .hlg [text] <--spawnEgg> <--force> <--baby>");
            return;
        }
        List<String> bruh = new ArrayList<>();
        for(String s : args) {
            if (!s.startsWith("--")) bruh.add(s);
        }

        boolean doSpawnEgg = oset.has("spawnEgg");
        ItemStack is = new ItemStack(doSpawnEgg?Items.BAT_SPAWN_EGG:Items.ARMOR_STAND);
        CompoundTag ct = is.getOrCreateTag();
        ByteTag vulnerable = ByteTag.ONE;
        ByteTag visible = ByteTag.ONE;
        ByteTag nogravity = ByteTag.ONE;
        ByteTag showName = ByteTag.ONE;
        ByteTag small = ByteTag.of(oset.has("baby"));
        String name = String.join(" ",bruh).replaceAll("&","§").trim();
        Vec3d loc = Cornos.minecraft.player.getPos();
        ListTag lt1 = new ListTag();
        lt1.add(DoubleTag.of(loc.x));
        lt1.add(DoubleTag.of(loc.y));
        lt1.add(DoubleTag.of(loc.z));
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("Pos",lt1);
        compoundTag.put("CustomName",StringTag.of("{\"text\":\""+name+"\"}"));
        compoundTag.put("CustomNameVisible", showName);
        compoundTag.put("Invisible",visible);
        compoundTag.put("Invulnerable",vulnerable);
        compoundTag.put("NoGravity",nogravity);
        compoundTag.put("Small",small);
        if (doSpawnEgg) compoundTag.put("id", StringTag.of("minecraft:armor_stand"));
        ct.put("EntityTag", compoundTag);
        is.setCustomName(Text.of("§r§cHologram §4generated by §lCornos"));
        is.setTag(ct);
        Cornos.minecraft.player.inventory.addPickBlock(is);

        super.onExecute(args);
    }
}