package fag.ware.client.module.impl.render;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.render.HasOutlineEvent;
import fag.ware.client.event.impl.render.RenderEntitiesGlowColorEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.ColorSetting;
import fag.ware.client.module.data.setting.impl.MultiStringSetting;
import fag.ware.client.util.SystemUtil;
import fag.ware.client.util.game.EntityUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;

@ModuleInfo(name = "ESP", category = ModuleCategory.RENDER, description = "Renders outlines around entities")
public class ESPModule extends AbstractModule {
    private final MultiStringSetting entities = new MultiStringSetting("Entities", new String[]{"Player"}, EntityUtil.getAllEntities());

    private final ColorSetting playerColor = new ColorSetting("Player color", new Color(230, 0, 55));
    private final ColorSetting animalColor = new ColorSetting("Animal color", new Color(0, 230, 55));
    private final ColorSetting monsterColor = new ColorSetting("Monster color", new Color(230, 0, 125));
    private final ColorSetting miscColor = new ColorSetting("Misc color", new Color(0, 120, 230));

    @Subscribe
    public void hasOutline(HasOutlineEvent event) {
        if (event.getEntity() != null) {
            String entityName = EntityType.getId(event.getEntity().getType()).getPath();
            if (entities.enabled(SystemUtil.toClassName(entityName))) {
                event.setCancelled(true);
            }
        }
    }

    @Subscribe
    public void renderEntitiesGlowColor(RenderEntitiesGlowColorEvent event) {
        if (event.getEntity() != null) {
            String entityName = EntityType.getId(event.getEntity().getType()).getPath();
            if (entities.enabled(SystemUtil.toClassName(entityName))) {
                event.setCancelled(true);
            }

            if (event.getEntity() instanceof PlayerEntity) {
                event.setColor(playerColor.getValue());
            } else {
                switch (event.getEntity().getType().getSpawnGroup()) {
                    case CREATURE -> event.setColor(animalColor.getValue());
                    case MONSTER -> event.setColor(monsterColor.getValue());
                    default -> event.setColor(miscColor.getValue());
                }
            }
        }
    }

    @Override
    public String getSuffix() {
        return "Glow";
    }
}