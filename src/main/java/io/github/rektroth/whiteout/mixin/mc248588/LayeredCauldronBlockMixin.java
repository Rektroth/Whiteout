package io.github.rektroth.whiteout.mixin.mc248588;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.InsideBlockEffectType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

/**
 * Layered cauldron block modifications for MC-248588 patch.
 */
@Mixin(LayeredCauldronBlock.class)
public abstract class LayeredCauldronBlockMixin {
    @Shadow
    private void handleEntityOnFireInside(BlockState state, Level level, BlockPos pos) { }

    /**
	 * Modifies the entity collision handling to check if the entity is either a player or mob griefing is enabled
	 * before applying a change to the cauldron state.
	 * @param instance              The inside block effect applier.
	 * @param insideBlockEffectType The inside block effect type.
	 * @param entityConsumer        boilerplate
	 * @param state                 The state of the cauldron.
	 * @param level                 The world the cauldron is in.
	 * @param serverLevel           The server world the cauldron is in.
	 * @param blockPos              The position of the cauldron.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/entity/InsideBlockEffectApplier;runBefore(Lnet/minecraft/world/entity/InsideBlockEffectType;Ljava/util/function/Consumer;)V",
			value = "INVOKE"
		),
		method = "entityInside"
	)
	private void canGrief2(
			InsideBlockEffectApplier instance,
			InsideBlockEffectType insideBlockEffectType,
			Consumer<Entity> entityConsumer,
			@Local(argsOnly = true) BlockState state,
			@Local(argsOnly = true) Level level,
			@Local ServerLevel serverLevel,
			@Local(ordinal = 1) BlockPos blockPos
	) {
		instance.runBefore(insideBlockEffectType, (collidedEntity) -> {
			if (collidedEntity.isOnFire()
				&& collidedEntity.mayInteract(serverLevel, blockPos)
				&& (collidedEntity instanceof Player || serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)) // the relevant change
			) {
				this.handleEntityOnFireInside(state, level, blockPos);
			}
		});
	}
}
