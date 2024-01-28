package dev.dumble.stats.listener;

import dev.dumble.stats.model.HeavenlyPlayer;
import dev.dumble.stats.mongodb.service.impl.PlayerService;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Optional;
import java.util.UUID;

public class BukkitAdapter implements Listener {

	@EventHandler
	public void entityDeath(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();

		Entity killer = entity.getKiller();
		if (killer == null) return;
		if (!entity.hasMetadata("heavenly_mob")) return;

		// todo: add a caching system for this
		PlayerService instance = PlayerService.getInstance();
		UUID uniqueId = killer.getUniqueId();

		Optional<HeavenlyPlayer> optionalPlayer = instance.retrieve(uniqueId);

		if (optionalPlayer.isPresent()) {
			HeavenlyPlayer player = optionalPlayer.get();
			Integer updatedMobKills = player.getMobKills() + 1;

			player.setMobKills(updatedMobKills);
			instance.update(player);

			return;
		}

		HeavenlyPlayer heavenlyPlayer = HeavenlyPlayer.builder()
				.setName(killer.getName())
				.setUniqueId(uniqueId)
				.setMobKills(1)
				.build();

		instance.create(heavenlyPlayer);
	}
}
