package fr.theskinter.mcdreams.objects.parc;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Hotel {

	@Getter @Setter private String name;
	@Getter @Setter private Integer room_number;
	@Getter private List<Chambre> chambres = new ArrayList<Chambre>();
	
	public Hotel(String name,int room_number) {
		this.name = name;
		this.room_number = room_number;
	}
	
	public void createChamber(int number) {
		getChambres().add(new Chambre(number));
	}
	
}
