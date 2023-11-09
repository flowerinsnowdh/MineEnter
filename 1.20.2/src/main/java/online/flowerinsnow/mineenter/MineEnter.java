package online.flowerinsnow.mineenter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import online.flowerinsnow.mineenter.exception.UnexpectedException;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MineEnter implements ClientModInitializer {
	public static final KeyBinding SECONDARY_OPEN_CHAT = new KeyBinding("mineenter.key.secondaryopenchat", GLFW.GLFW_KEY_KP_ENTER, "mineenter.key.category");

	private static final Method METHOD_OPEN_CHAT_SCREEN;

	static {
		try {
			//noinspection JavaReflectionMemberAccess
			METHOD_OPEN_CHAT_SCREEN = MinecraftClient.class.getDeclaredMethod("method_29041", String.class);
			METHOD_OPEN_CHAT_SCREEN.setAccessible(true);
		} catch (NoSuchMethodException e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void onInitializeClient() {
		KeyBindingHelper.registerKeyBinding(SECONDARY_OPEN_CHAT);
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			MinecraftClient mc = MinecraftClient.getInstance();
			if (mc.getOverlay() == null && mc.currentScreen == null) {
				while (SECONDARY_OPEN_CHAT.wasPressed()) {
					try {
						METHOD_OPEN_CHAT_SCREEN.invoke(mc, "");
					} catch (IllegalAccessException | InvocationTargetException e) {
						throw new UnexpectedException(e);
					}
                }
			}
		});
	}
}