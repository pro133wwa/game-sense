package Game.Sense.client.module.feature.PLAYER;

import Game.Sense.client.Helper.EventTarget;
import Game.Sense.client.Helper.events.impl.packet.EventReceivePacket;
import Game.Sense.client.Helper.events.impl.player.EventUpdate;
import Game.Sense.client.Helper.events.impl.render.EventRender2D;
import Game.Sense.client.module.Module;
import Game.Sense.client.module.feature.ModuleCategory;
import Game.Sense.client.UI.Settings.impl.BooleanSetting;
import Game.Sense.client.UI.Settings.impl.NumberSetting;
import Game.Sense.client.Helper.Utility.movement.MovementUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class FreeCam extends Module {

    private final NumberSetting speed = new NumberSetting("Flying Speed", 0.5F, 0.1F, 1F, 0.1F, () -> true);


    private final BooleanSetting reallyWorld = new BooleanSetting("ReallyWorld Bypass", false, () -> true);

    double x, y, z;

    public FreeCam() {
        super("FreeCam", "��������� ������ � ��������� ������", ModuleCategory.PLAYER);
        addSettings(speed, reallyWorld);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (mc.player.isDead) {
            toggle();
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            if (reallyWorld.getBoolValue() && mc.player != null || mc.world != null) {
                event.setCancelled(true);
            }
        }
    }

    public void onEnable() {
        super.onEnable();
        if (mc.player.isDead) {
            toggle();
        }
        x = mc.player.posX;
        y = mc.player.posY;
        z = mc.player.posZ;
        EntityOtherPlayerMP ent = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        ent.inventory = mc.player.inventory;
        ent.inventoryContainer = mc.player.inventoryContainer;
        ent.setHealth(mc.player.getHealth());
        ent.setPositionAndRotation(this.x, mc.player.getEntityBoundingBox().minY, this.z, mc.player.rotationYaw, mc.player.rotationPitch);
        ent.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-1, ent);
    }

    @EventTarget
    public void onScreen(EventRender2D e) {
        ScaledResolution sr = new ScaledResolution(mc);
        String yCoord = "" + Math.round(mc.player.posY - y);

        String str = "Y: " + yCoord;
        mc.rubik_18.drawStringWithOutline(str, (sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(str)) / 1.98, sr.getScaledHeight() / 1.8 - 20, -1);

    }

    @EventTarget
    public void onPreMotion(EventUpdate e) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        mc.player.motionY = 0;
        if (mc.gameSettings.keyBindJump.pressed) {
            mc.player.motionY = speed.getNumberValue();
        }
        if (mc.gameSettings.keyBindSneak.pressed) {
            mc.player.motionY = -speed.getNumberValue();
        }
        mc.player.noClip = true;
        MovementUtils.setSpeed(speed.getNumberValue());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.setPosition(x, y, z);
        mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.01, mc.player.posZ, mc.player.onGround));
        mc.player.capabilities.isFlying = false;
        mc.player.noClip = false;
        mc.world.removeEntityFromWorld(-1);
    }
}
