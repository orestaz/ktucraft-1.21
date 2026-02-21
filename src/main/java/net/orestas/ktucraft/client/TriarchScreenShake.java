package net.orestas.ktucraft.client;

public class TriarchScreenShake {

    private static int ticksLeft = 0;
    private static int maxTicks = 0;
    private static float strength = 0f;

    public static void trigger(int durationTicks, float newStrength) {
        if (durationTicks <= 0 || newStrength <= 0f) return;
        // stackinam: ilgesnis arba stipresnis laimi
        if (durationTicks > ticksLeft) {
            ticksLeft = durationTicks;
            maxTicks = durationTicks;
        }
        if (newStrength > strength) {
            strength = newStrength;
        }
    }

    public static void clientTick() {
        if (ticksLeft > 0) {
            ticksLeft--;
            if (ticksLeft == 0) strength = 0f;
        }
    }

    /** kviečia mixin kiekvieną frame */
    public static float getStrength(float tickDelta) {
        if (ticksLeft <= 0 || maxTicks <= 0) return 0f;
        float t = (ticksLeft - tickDelta) / (float) maxTicks; // 1 -> 0
        if (t < 0f) t = 0f;
        return strength * t;
    }
}