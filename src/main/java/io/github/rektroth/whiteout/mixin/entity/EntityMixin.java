package io.github.rektroth.whiteout.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput {
	@ModifyVariable(at = @At("HEAD"), method = "setPos(DDD)V", ordinal = 0)
	private double fixItemXPositionDesync(double x){
		return (Entity)(Object)this instanceof ItemEntity ? MathHelper.lfloor(x * 4096.0D) * (1 / 4096.0D) : x;
	}

	@ModifyVariable(at = @At("HEAD"), method = "setPos(DDD)V", ordinal = 1)
	private double fixItemYPositionDesync(double y){
		return (Entity)(Object)this instanceof ItemEntity ? MathHelper.lfloor(y * 4096.0D) * (1 / 4096.0D) : y;
	}

	@ModifyVariable(at = @At("HEAD"), method = "setPos(DDD)V", ordinal = 2)
	private double fixItemZPositionDesync(double z){
		return (Entity)(Object)this instanceof ItemEntity ? MathHelper.lfloor(z * 4096.0D) * (1 / 4096.0D) : z;
	}
}
