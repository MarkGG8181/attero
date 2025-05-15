package fag.ware.client.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author markuss
 */
@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {
    @Accessor("boundKey")
    InputUtil.Key getBoundKey();
}
