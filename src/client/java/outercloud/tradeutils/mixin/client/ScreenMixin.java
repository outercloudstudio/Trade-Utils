package outercloud.tradeutils.mixin.client;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface ScreenMixin {
    @Invoker()
    <T extends Element & Drawable & Selectable> T invokeAddDrawableChild(T drawableElement);

    @Invoker()
    void invokeRemove(Element drawableElement);
}
