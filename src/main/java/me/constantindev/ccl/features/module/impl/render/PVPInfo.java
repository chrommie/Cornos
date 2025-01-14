package me.constantindev.ccl.features.module.impl.render;

import me.constantindev.ccl.Cornos;
import me.constantindev.ccl.etc.config.MConfNum;
import me.constantindev.ccl.etc.helper.Renderer;
import me.constantindev.ccl.features.module.Module;
import me.constantindev.ccl.features.module.ModuleType;
import me.constantindev.ccl.features.module.impl.external.Hud;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class PVPInfo extends Module {
    public static LivingEntity current = null;
    MConfNum trackDistance = new MConfNum("trackDistance", 20.0, 100, 3);
    int distLatest = -1;

    public PVPInfo() {
        super("PVPInfo", "Shows info about the nearest player", ModuleType.RENDER);
        this.mconf.add(trackDistance);
    }

    @Override
    public void onExecute() {
        Vec3d pos = Cornos.minecraft.player.getPos();
        PlayerEntity closestSoFar = null;
        for (Entity entity : Cornos.minecraft.world.getEntities()) {
            if (entity.getUuid().equals(Cornos.minecraft.player.getUuid())) continue;
            if (entity instanceof PlayerEntity) {
                Vec3d ep = entity.getPos();
                double dist = pos.distanceTo(ep);
                if (dist > trackDistance.getValue()) continue;
                if (closestSoFar == null || dist < closestSoFar.getPos().distanceTo(pos)) {
                    closestSoFar = (PlayerEntity) entity;
                    distLatest = (int) dist;
                }
            }
        }
        current = closestSoFar;
        super.onExecute();
    }

    @Override
    public void onDisable() {
        current = null;
        super.onDisable();
    }

    @Override
    public void onHudRender(MatrixStack ms, float td) {
        if (current != null) {
            Window w = Cornos.minecraft.getWindow();
            int w1 = w.getScaledWidth() / 2;
            int h = w.getScaledHeight() / 2;
            int width = 100;
            int height = 32;
            int x1 = w1 + 5;
            int x2 = x1 + width;
            int y1 = h + 5;
            int y2 = y1 + height;
            double innerCompScale = 1;
            String uname = current.getEntityName();
            current.setCustomName(Text.of("DoNotRenderThisUsernameISwearToGod")); // it works but im not proud
            DrawableHelper.fill(ms, x1, y1, x2, y2, new Color(20, 20, 20, 100).getRGB());
            Renderer.renderLineScreen(new Vec3d(x1, y1, 0), new Vec3d(x2, y1, 0), Hud.themeColor.getColor(), 2);
            Renderer.renderLineScreen(new Vec3d(x2, y1, 0), new Vec3d(x2, y2, 0), Hud.themeColor.getColor(), 2);
            Renderer.renderLineScreen(new Vec3d(x2, y2, 0), new Vec3d(x1, y2, 0), Hud.themeColor.getColor(), 2);
            Renderer.renderLineScreen(new Vec3d(x1, y2, 0), new Vec3d(x1, y1, 0), Hud.themeColor.getColor(), 2);
            InventoryScreen.drawEntity(x1 + 8, y1 + 30, 15, (float) -(w1 / 10), (float) -(h / 10), current);
            Cornos.minecraft.textRenderer.draw(ms, uname, x2 - Cornos.minecraft.textRenderer.getWidth(uname) - 1, y1 + 2, 0xFFFFFF);
            GL11.glScaled(innerCompScale, innerCompScale, innerCompScale);
            Cornos.minecraft.textRenderer.draw(ms, "Distance: " + distLatest, (float) ((x1 + 8 + 7 + 2) / innerCompScale), (float) ((y1 + 11) / innerCompScale), Color.WHITE.getRGB());
            Cornos.minecraft.textRenderer.draw(ms, "Health: " + ((int) Math.floor(current.getHealth())) + " / " + ((int) Math.floor(current.getMaxHealth())), (float) ((x1 + 8 + 7 + 2) / innerCompScale), (float) ((y1 + 11 + 10) / innerCompScale), Color.WHITE.getRGB());
            GL11.glScaled(1 / innerCompScale, 1 / innerCompScale, 1 / innerCompScale);
            current.setCustomName(null);
        }
        super.onHudRender(ms, td);
    }
}
