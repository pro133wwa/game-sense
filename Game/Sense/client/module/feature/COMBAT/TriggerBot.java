package Game.Sense.client.module.feature.COMBAT;

import Game.Sense.client.GameSense;
import Game.Sense.client.Helper.EventTarget;
import Game.Sense.client.Helper.events.impl.player.EventUpdate;
import Game.Sense.client.module.Module;
import Game.Sense.client.module.feature.ModuleCategory;
import Game.Sense.client.UI.UwU.NAXNADO.Friend;
import Game.Sense.client.UI.Settings.impl.BooleanSetting;
import Game.Sense.client.UI.Settings.impl.NumberSetting;
import Game.Sense.client.Helper.Utility.movement.MovementUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class TriggerBot extends Module {

    public static NumberSetting range;
    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting onlyCrit = new BooleanSetting("Only Crits", false, () -> true);
    public static NumberSetting critFallDist = new NumberSetting("Fall Distance", 0.2F, 0.08F, 1, 0.01f, () -> onlyCrit.getBoolValue());

    public TriggerBot() {
        super("TriggerBot", "������������� ������� ���� ��� ������� �� ��������", ModuleCategory.COMBAT);
        players = new BooleanSetting("Players", true, () -> true);
        mobs = new BooleanSetting("Mobs", false, () -> true);
        range = new NumberSetting("Trigger Range", 4, 1, 6, 0.1f, () -> true);
        addSettings(range, players, mobs, onlyCrit, critFallDist);
    }

    public static boolean canTrigger(EntityLivingBase player) {
        for (Friend friend : GameSense.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
                continue;
            }
            return false;
        }

        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !players.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !mobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityMob && !mobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !mobs.getBoolValue()) {
                return false;
            }
        }
        return player != mc.player;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        Entity entity = mc.objectMouseOver.entityHit;
        if (entity == null || mc.player.getDistanceToEntity(entity) > range.getNumberValue() || entity instanceof EntityEnderCrystal || entity.isDead || ((EntityLivingBase) entity).getHealth() <= 0.0f) {
            return;
        }

        if (MovementUtils.isBlockAboveHead()) {
            if (!(mc.player.fallDistance >= critFallDist.getNumberValue()) && mc.player.getCooledAttackStrength(0.8F) == 1 && onlyCrit.getBoolValue() && !mc.player.isOnLadder() && !mc.player.isInLiquid() && !mc.player.isInWeb && mc.player.getRidingEntity() == null) {
                return;
            }
        } else {
            if (mc.player.fallDistance != 0 && onlyCrit.getBoolValue() && !mc.player.isOnLadder() && !mc.player.isInLiquid() && !mc.player.isInWeb && mc.player.getRidingEntity() == null) {
                return;
            }
        }

        if (canTrigger((EntityLivingBase) entity)) {
            if (mc.player.getCooledAttackStrength(0) == 1) {
                mc.playerController.attackEntity(mc.player, entity);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
}

