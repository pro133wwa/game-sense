package Game.Sense.client.event.events.impl.packet;

import Game.Sense.client.event.events.callables.EventCancellable;
import net.minecraft.network.Packet;

public class EventSendPacket extends EventCancellable {

    private final Packet<?> packet;

    public EventSendPacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }
}
