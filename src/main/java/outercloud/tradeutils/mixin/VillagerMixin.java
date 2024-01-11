package outercloud.tradeutils.mixin;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import outercloud.tradeutils.TradeUtils;
import outercloud.tradeutils.VillagerMixinBridge;

@Mixin(VillagerEntity.class)
public class VillagerMixin implements VillagerMixinBridge {
	private boolean customTrades = false;

	public void enableCustomTrades(){
		customTrades = true;
	}

	@Inject(at = @At("TAIL"), method = "writeCustomDataToNbt", locals = LocalCapture.CAPTURE_FAILHARD)
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
		nbt.putBoolean("custom_trades", customTrades);
	}

	@Inject(at = @At("TAIL"), method = "readCustomDataFromNbt", locals = LocalCapture.CAPTURE_FAILHARD)
	private void readCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
		customTrades = nbt.getBoolean("custom_trades");
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo ci) {
		VillagerEntity me = (VillagerEntity) (Object) this;

		if(me.getWorld().getTimeOfDay() == 1) me.restock();
	}

	@Inject(at = @At("HEAD"), method = "fillRecipes", cancellable = true)
	private void fillRecipes(CallbackInfo ci) {
		if(!customTrades) return;

		ci.cancel();
	}
}