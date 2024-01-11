package outercloud.tradeutils.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import outercloud.tradeutils.TradeUtils;
import outercloud.tradeutils.TradeUtilsClient;

import java.util.ArrayList;
import java.util.List;

@Mixin(MerchantScreen.class)
public class MerchantScreenMixin {
	private boolean editMode;
	private ButtonWidget editButton;
	private ArrayList<TradeUtilsClient.ElementDrawableSelectable> editModeWidgets = new ArrayList<>();

	@Inject(at = @At("TAIL"), method = "init")
	private void init(CallbackInfo ci) {
		MerchantScreen me = (MerchantScreen) (Object) this;
		HandledScreenMixin handledScreenMixin = (HandledScreenMixin) this;
		ScreenMixin screenMixin = (ScreenMixin) this;

		int x = (me.width - handledScreenMixin.getBackgroundWidth()) / 2 + handledScreenMixin.getBackgroundWidth() - 32;
		int y = (me.height - handledScreenMixin.getBackgroundHeight()) / 2;

		editButton = ButtonWidget.builder(Text.of("edit"), widget -> {
			editMode = !editMode;

			if(editMode) {
				enterEditMode();
			} else {
				exitEditMode();
			}
		}).position(x, y).size(32, 16).build();

		screenMixin.invokeAddDrawableChild(editButton);
	}

	private void enterEditMode() {
		ButtonWidget editModeWidget = ButtonWidget.builder(Text.of("Edit Widget"), widget -> {}).build();
		editModeWidgets.add((TradeUtilsClient.ElementDrawableSelectable) editModeWidget);

		for (TradeUtilsClient.ElementDrawableSelectable widget: editModeWidgets) {
			ScreenMixin screenMixin = (ScreenMixin) this;
			screenMixin.invokeAddDrawableChild(widget);
		}
	}

	private void exitEditMode() {
		for (TradeUtilsClient.ElementDrawableSelectable widget: editModeWidgets) {
			ScreenMixin screenMixin = (ScreenMixin) this;
			screenMixin.invokeRemove(widget);
		}
	}

	@Inject(at = @At("TAIL"), method = "render", locals = LocalCapture.CAPTURE_FAILHARD)
	private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {}
}