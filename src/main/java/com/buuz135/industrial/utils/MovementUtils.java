package com.buuz135.industrial.utils;

import com.buuz135.industrial.proxy.block.BlockConveyor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MovementUtils {

    public static void handleConveyorMovement(Entity entity, EnumFacing direction, BlockPos pos, BlockConveyor.EnumType type) {
        if (entity instanceof EntityPlayer && entity.isSneaking()) return;
        if (entity.posY - pos.getY() > 0.3 && !type.isVertical()) return;

        AxisAlignedBB collision = entity.world.getBlockState(pos).getBlock().getCollisionBoundingBox(entity.world.getBlockState(pos), entity.world, pos).offset(pos);
//        if (direction == EnumFacing.NORTH || direction == EnumFacing.SOUTH){
//            collision = collision.contract(-0.1,0,0);
//            collision = collision.contract(0.1,0,0);
//        }
//        if (direction == EnumFacing.EAST || direction == EnumFacing.WEST) {
//            collision = collision.contract(0,0,-0.1);
//            collision = collision.contract(0,0,0.1);
//        }
        if (!type.isVertical() && !collision.grow(0.01).intersects(entity.getEntityBoundingBox())) return;

        //DIRECTION MOVEMENT
        double speed = 0.2;
        if (type.isFast()) speed *= 2;
        Vec3d vec3d = new Vec3d(speed * direction.getDirectionVec().getX(), speed * direction.getDirectionVec().getY(), speed * direction.getDirectionVec().getZ());
        if (type.isVertical()) {
            vec3d = vec3d.addVector(0, type.isUp() ? 0.258 : -0.05, 0);
            entity.onGround = false;
        }

        //CENTER
        if (direction == EnumFacing.NORTH || direction == EnumFacing.SOUTH) {
            if (entity.posX - pos.getX() < 0.45) {
                vec3d = vec3d.addVector(0.08, 0, 0);
            } else if (entity.posX - pos.getX() > 0.55) {
                vec3d = vec3d.addVector(-0.08, 0, 0);
            }
        }
        if (direction == EnumFacing.EAST || direction == EnumFacing.WEST) {
            if (entity.posZ - pos.getZ() < 0.45) {
                vec3d = vec3d.addVector(0, 0, 0.08);
            } else if (entity.posZ - pos.getZ() > 0.55) {
                vec3d = vec3d.addVector(0, 0, -0.08);
            }
        }

        entity.motionX = vec3d.x;
        if (vec3d.y != 0) entity.motionY = vec3d.y;
        entity.motionZ = vec3d.z;
    }
}
