package fr.theskinter.mcdreams.utils.creators;

import lombok.Getter;

public class TabListCreator {

	@Getter
	public String header;
	@Getter
	public String footer;
	
	public TabListCreator setHeader(String header) {
		this.header = header;
		return this;
	}
	
	public TabListCreator setFooter(String footer) {
		this.footer = footer;
		return this;
	}
	
	
}
