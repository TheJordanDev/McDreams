package fr.theskinter.mcdreams.objects.parc;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class Chambre {

	@Getter private Integer numero;
	@Getter @Setter private UUID owner;

	public Chambre(int numero) {
		this.numero = numero;
	}
	
}
