package org.d1p4k.netherportalboom.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.NetherPortal;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.d1p4k.netherportalboom.NetherPortalBoom;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherPortal.class)
public class NetherPortalMixin {
    @Shadow private @Nullable BlockPos lowerCorner;

    @Shadow @Final private WorldAccess world;

    @Inject(method = "createPortal", at = @At("HEAD"), cancellable = true)
    public void cancelPortalCreationAndExplode(CallbackInfo ci) {
        for (int x = -5; x < 5; x++) {
            for (int y = -5; y < 5; y++) {
                for (int z = -5; z < 5; z++) {
                    if(world.getBlockState(lowerCorner.add(x, y, z)).getBlock().equals(Blocks.OBSIDIAN)){
                        world.breakBlock(lowerCorner.add(x, y, z), true);
                    }
                }
            }
        }
        if(world instanceof ServerWorld world) {
            world.createExplosion(null, world.getDamageSources().badRespawnPoint(new Vec3d(0,0,0)),
                    new ExplosionBehavior(), lowerCorner.getX(), lowerCorner.getY(), lowerCorner.getZ(),
                    NetherPortalBoom.explosionRadius(), true, World.ExplosionSourceType.BLOCK);
        }
        ci.cancel();
    }
}
