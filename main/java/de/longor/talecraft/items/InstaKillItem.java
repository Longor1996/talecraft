package de.longor.talecraft.items;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InstaKillItem extends TCItem {
	
	public InstaKillItem() {
        this.setCreativeTab(TaleCraftTabs.tab_TaleCraftTab);
	}
    
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    	target.setHealth(0);
    	target.isDead = true;
        return false;
    }
	
}
