package com.unseen.db.proxy;

import com.unseen.db.Main;
import com.unseen.db.util.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void registerItemRenderer(Item item, int meta, String id) {
    }
    public void init() {
        Main.network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.CHANNEL_NAME);


    }
}
