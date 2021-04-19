package me.constantindev.ccl.gui.widget;

import me.constantindev.ccl.Cornos;
import me.constantindev.ccl.etc.helper.RenderHelper;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class RoundedButtonWidget extends AbstractPressableButtonWidget {
    Runnable onPressed;
    int r;

    public RoundedButtonWidget(int x, int y, int width, int height, Text message, Runnable onPress) {
        super(x, y, width, height, message);
        this.r = 5;
        this.onPressed = onPress;
    }

    public RoundedButtonWidget(int x, int y, int width, int height, int roundness, Text message, Runnable onPress) {
        super(x, y, width, height, message);
        this.onPressed = onPress;
        this.r = roundness;
    }

    @Override
    public void onPress() {
        onPressed.run();
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Color c;
        if (this.isHovered()) {
            c = new Color(12, 12, 12, 100);
        } else {
            c = new Color(30, 30, 30, 100);
        }
        RenderHelper.renderRoundedQuad(new Vec3d(x, y, 0), new Vec3d(x + width, y + height, 0), r, c);
        drawCenteredText(matrices, Cornos.minecraft.textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, Color.white.getRGB());
    }
}