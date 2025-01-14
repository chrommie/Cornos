package me.constantindev.ccl.etc.render.particles;

import me.constantindev.ccl.etc.helper.Renderer;

import java.awt.*;
import java.util.Random;

public class LargeParticles extends Particles {
    public LargeParticles(int amount) {
        super(amount);
        Random r = new Random();
        for (Particle particle : particles) {
            particle.color = new Color(r.nextInt(0xFF), r.nextInt(0xFF), r.nextInt(0xFF), 130);
            particle.cache.add(r.nextDouble() * 50);
            particle.speed /= 5;
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render() {
        for (Particle particle : particles) {
            Renderer.renderCircle(particle.posX, particle.posY, particle.cache.get(0), particle.color, 2);
        }
        super.render();
    }
}
