package org.d1p4k.netherportalboom.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.AreaHelper;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.d1p4k.netherportalboom.NetherPortalBoom;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AreaHelper.class)
public class TestMixin {

    @Shadow @Final private Direction.Axis axis;

    @Shadow private @Nullable BlockPos lowerCorner;

    @Shadow @Final private WorldAccess world;

    @Inject(method = "createPortal", at = @At("HEAD"), cancellable = true)
    public void mixin(CallbackInfo ci) {
        for (int x = -5; x < 5; x++) {
            for (int y = -5; y < 5; y++) {
                for (int z = -5; z < 5; z++) {
                    if(world.getBlockState(lowerCorner.add(x, y, z)).getBlock().equals(Blocks.OBSIDIAN)){
                        world.breakBlock(lowerCorner.add(x, y, z), true);
                    }
                }
            }
        }
        if(world instanceof World) {
            ((World) world).createExplosion(null, DamageSource.badRespawnPoint(),
                    new ExplosionBehavior(), lowerCorner.getX(), lowerCorner.getY(), lowerCorner.getZ(),
                    NetherPortalBoom.explosionRadius(), true, Explosion.DestructionType.BREAK);
        }
        ci.cancel();
    }
}
