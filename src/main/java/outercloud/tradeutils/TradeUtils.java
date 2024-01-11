package outercloud.tradeutils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradeUtils implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("trade-utils");

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerCommands(dispatcher, registryAccess));
	}

	private void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		dispatcher.register(CommandManager.literal("trades")
				.requires(source -> source.hasPermissionLevel(4))
				.then(CommandManager.argument("entity", EntityArgumentType.entity())
						.then(CommandManager.literal("add")
								.then(CommandManager.argument("in", ItemStackArgumentType.itemStack(registryAccess))
										.then(CommandManager.argument("in_amount", IntegerArgumentType.integer(1))
												.then(CommandManager.argument("out", ItemStackArgumentType.itemStack(registryAccess))
														.then(CommandManager.argument("out_amount", IntegerArgumentType.integer(1))
																.executes(context -> {
																	Entity entity = EntityArgumentType.getEntity(context, "entity");
																	int inAmount = IntegerArgumentType.getInteger(context, "in_amount");
																	ItemStack inItemStack = ItemStackArgumentType.getItemStackArgument(context, "in").createStack(inAmount, false);
																	int outAmount = IntegerArgumentType.getInteger(context, "out_amount");
																	ItemStack outItemStack = ItemStackArgumentType.getItemStackArgument(context, "out").createStack(outAmount, false);

																	if(!(entity instanceof VillagerEntity villager)) return 0;

																	TradeOfferList tradeOfferList = new TradeOfferList();

																	TradeOffer tradeOffer = new TradeOffer(inItemStack, outItemStack, 1, 0, 0);

																	tradeOfferList.add(tradeOffer);

																	villager.setOffers(tradeOfferList);

																	return 1;
																})
														)
												)
										)
								)
						)
				)
		);
	}
}