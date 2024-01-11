package outercloud.tradeutils;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;

public class TradeUtilsClient implements ClientModInitializer {
	public interface ElementDrawableSelectable extends Element, Drawable, Selectable {}

	@Override
	public void onInitializeClient() {

	}
}