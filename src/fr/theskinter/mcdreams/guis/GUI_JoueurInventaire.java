package fr.theskinter.mcdreams.guis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.events.GUIInteractEvent;
import fr.theskinter.mcdreams.objects.Joueur;
import fr.theskinter.mcdreams.objects.JoueurInventaire;
import fr.theskinter.mcdreams.utils.creators.GUICreator;
import fr.theskinter.mcdreams.utils.creators.ItemCreator;
import lombok.Getter;

@SuppressWarnings("deprecation")
public class GUI_JoueurInventaire extends GUICreator {
	
	public GUI_JoueurInventaire(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@Getter public Map<UUID,Integer> backupInventoryPage = new HashMap<UUID,Integer>();
	@Getter public Map<UUID,UUID> playerSelectedJoueurInventory = new HashMap<UUID,UUID>();
	
	private ItemStack next_btn = new ItemCreator().setName("§e§lSuivant").setMaterial(Material.GOLD_PLATE).build();
	private ItemStack prev_btn = new ItemCreator().setName("§6§lPrécédent").setMaterial(Material.IRON_PLATE).build();

	private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
	
	
	public Inventory open(UUID uuid,Integer page) {
		setName("§9§lInventaire");
		setBackGround(new ItemCreator().setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).setName(" ").build());
		setMaxLine(6);
		setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
		setSlot(0, exit());
		if (!getPlayerSelectedJoueurInventory().containsKey(uuid)) return super.build();
		if (!Joueur.doesJoueurExist(getPlayerSelectedJoueurInventory().get(uuid))) return super.build();
		Joueur target = Joueur.getJoueur(getPlayerSelectedJoueurInventory().get(uuid));
		List<ItemStack> items = new ArrayList<>();
		Iterator<? extends ItemStack> it_items = target.getOfflinePlayerIfHasPlayed().getPlayer().getEnderChest().iterator();
		it_items.forEachRemaining(items::add);
		Collections.sort(items,JoueurInventaire.sorter);
		getBackupInventoryPage().put(uuid, page);
			if (!items.isEmpty()) {
				if (page<1) {
					open(uuid,1);
				} else if (page == 1) {
					int i = 0; 
					for (int slot=9;slot<getSlots().size()-9;slot++) { 
						if (items.size()<=i) {break;}
						setSlot(slot, items.get(i));
						i++;
					}
					if (items.size()>i) { 
						setSlot(50, next_btn);
					}
				} else if (page > 1){
					int i = ((getLine()-18)*page)-27;
					for (int slot=9;slot<getSlots().size()-9;slot++) {
						if (items.size()<=i) { break;}
						setSlot(slot, items.get(i));	
						i++;
					}
					setSlot(48, prev_btn);
					if (items.size()>i) { 
						setSlot(50, next_btn);
					}
				}
			} else {
				setSlot(22, new ItemCreator().setName("§7§lAucun Objet !!").setMaterial(Material.PAPER).build());
			}
			return build();
		}
	
	@EventHandler
	public void onGUIInteract(GUIInteractEvent event) {
		if (event.getGui_id().equals(getId())) {
			Player player = (Player) event.getEvent().getWhoClicked();
			event.setCancelled(true);
			ItemStack item = event.getEvent().getCurrentItem();
			if (item == null) return;
			if (item.isSimilar(exit())) {
				player.openInventory(McDreams.instance.getMcDreamsGUI().playerInfoMenu.build(player.getUniqueId(), getPlayerSelectedJoueurInventory().get(player.getUniqueId())));
			} else if (item.isSimilar(next_btn)) {
				player.openInventory(open(player.getUniqueId(), getBackupInventoryPage().get(player.getUniqueId())+1));
			} else if (item.isSimilar(prev_btn)) {
				player.openInventory(open(player.getUniqueId(), getBackupInventoryPage().get(player.getUniqueId())-1));
			}
		}
	}
	
	public Integer getAttractionIndex(UUID uuid,Integer slot) {
		if (!getBackupInventoryPage().containsKey(uuid)) return 0;
		Integer page = getBackupInventoryPage().get(uuid);
		if (page==1) {
			int i = 0; 
			for (int slota=9;slota<getSlots().size()-9;slota++) { 
				if (slota == slot) {
					return i;
				}
				i++;
			}
		} else {
			int i = ((getLine()-18)*page)-27;
			for (int slota=9;slota<getSlots().size()-9;slota++) {
				if (slota == slot) {
					return i;
				}
				i++;
			}
		}
		return 0;
	}
	

}
