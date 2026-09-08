package org.example.mixin;

import com.zenith.Proxy;
import org.example.ExamplePlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(Proxy.class)
public class ExampleMixin {
    @Inject(method = "start", at = @At(
        value = "INVOKE",
        target = "Lcom/zenith/Proxy;startServer()V"
    ))
    public void inject() {
        ExamplePlugin.LOG.info("hello from example mixin");
    }
}
