package uk.ac.man.cs.eventlite.controllers;

import org.jsoup.nodes.Document;

public interface NiceText {
	   public String extract(String url);
	   public String extract(Document document);
}
