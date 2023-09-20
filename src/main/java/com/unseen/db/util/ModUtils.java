package com.unseen.db.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class ModUtils {
    public static AxisAlignedBB makeBox(Vec3d pos1, Vec3d pos2) {
        return new AxisAlignedBB(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }
}
