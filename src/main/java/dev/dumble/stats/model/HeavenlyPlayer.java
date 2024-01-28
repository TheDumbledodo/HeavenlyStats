package dev.dumble.stats.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.Objects;
import java.util.UUID;

@Getter @Setter
@Builder(setterPrefix = "set")
public class HeavenlyPlayer implements EntityConverter {

    private UUID uniqueId;
	private String name;
	private Integer mobKills;

	@Override
	public UUID getKey() {
		return this.getUniqueId();
	}

	@Override
	public Document toDocument() {
		return new Document()
				.append("uniqueId", this.getUniqueId())
				.append("name", this.getName())
				.append("mobKills", this.getMobKills());
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) return true;
		if (!(object instanceof HeavenlyPlayer)) return false;

		return this.getName().equalsIgnoreCase(((HeavenlyPlayer) object).getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getUniqueId(), this.getName());
	}
}
