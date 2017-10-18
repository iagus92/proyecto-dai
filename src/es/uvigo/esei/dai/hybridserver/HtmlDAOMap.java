package es.uvigo.esei.dai.hybridserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HtmlDAOMap implements HtmlDAO {

	private final Map<String, String>  pages;
	
	/**
	 * Constructor que crea un mapa con webs de ejemplo.
	 */
	public HtmlDAOMap() {
		this.pages = new HashMap<>();
		
		String[][] pages = new String[][] {
		    { "6df1047e-cf19-4a83-8cf3-38f5e53f7725", "This is the html page 6df1047e-cf19-4a83-8cf3-38f5e53f7725." },
		    { "79e01232-5ea4-41c8-9331-1c1880a1d3c2", "This is the html page 79e01232-5ea4-41c8-9331-1c1880a1d3c2." },
		    { "a35b6c5e-22d6-4707-98b4-462482e26c9e", "This is the html page a35b6c5e-22d6-4707-98b4-462482e26c9e." },
		    { "3aff2f9c-0c7f-4630-99ad-27a0cf1af137", "This is the html page 3aff2f9c-0c7f-4630-99ad-27a0cf1af137." },
		    { "77ec1d68-84e1-40f4-be8e-066e02f4e373", "This is the html page 77ec1d68-84e1-40f4-be8e-066e02f4e373." },
		    { "8f824126-0bd1-4074-b88e-c0b59d3e67a3", "This is the html page 8f824126-0bd1-4074-b88e-c0b59d3e67a3." },
		    { "c6c80c75-b335-4f68-b7a7-59434413ce6c", "This is the html page c6c80c75-b335-4f68-b7a7-59434413ce6c." },
		    { "f959ecb3-6382-4ae5-9325-8fcbc068e446", "This is the html page f959ecb3-6382-4ae5-9325-8fcbc068e446." },
		    { "2471caa8-e8df-44d6-94f2-7752a74f6819", "This is the html page 2471caa8-e8df-44d6-94f2-7752a74f6819." },
		    { "fa0979ca-2734-41f7-84c5-e40e0886e408", "This is the html page fa0979ca-2734-41f7-84c5-e40e0886e408." }
		};
		
		for (String[] page : pages) {
			this.pages.put(page[0], page[1]);
		}
		
	}
	
	/**
	 * Constructor que recibe un mapa con webs.
	 * @param pages Mapa de páginas web <uuid,contenido>.
	 */
	public HtmlDAOMap(Map<String, String>  pages) {
		this.pages = pages;
	}

	@Override
	public String getHtmlPage(String uuid) {
		return pages.get(uuid);
	}

	@Override
	public String getHtmlList() {
		StringBuilder sb = new StringBuilder();	
		Iterator<String> iterator = new ArrayList<String>(pages.keySet()).iterator();
		
		while (iterator.hasNext()) {
			String uuid = iterator.next();
			sb.append("<a href='/html?uuid="+ uuid +"' target='_blank'>"+ uuid +"</a><br />");
		}
		return sb.toString();
	}

	@Override
	public void createHtmlPage(String uuid, String content) {
		this.pages.put(uuid, content);
	}
	
	@Override
	public void deleteHtmlPage(String uuid) {
		this.pages.remove(uuid);
		
	}
	
	@Override
	public boolean containsPage(String uuid){
		return pages.containsKey(uuid);
	}
}
