package es.uvigo.esei.dai.hybridserver;

import java.util.Map;
import java.util.UUID;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.MIME;

public class XsdManager {

	private final XsdDAO XsdDao;
	private Map<String,String> params;
	private String uuid;
	private HTTPResponseStatus status;
	private String content;
	private String type;

	/**
	 * Constructor que recibe una instancia de la interfaz XsdDAO.
	 * @param XsdDao Interfaz que permite interactuar con las webs almacenadas.
	 */
	public XsdManager(XsdDAO XsdDao) {
		this.XsdDao = XsdDao;
	}
	
	/**
	 * Recibe una petición GET y genera una respuesta que puede ser:
	 * Lista de todas las páginas, una sola página o un error 404.
	 * @param request Petición HTTP.
	 */
	public void methodGet(HTTPRequest request, Map<String, ServersDAO> servers){
		params = request.getResourceParameters();
		uuid = params.get("uuid");

		// Comprueba si se recibe el parámetro uuid.
		if(uuid == null) { 
			status = HTTPResponseStatus.S200;
			StringBuilder sc = new StringBuilder();
			sc.append("<h1>Local Server</h1>");
			sc.append(XsdDao.getXsdList()); // Recupera una lista de páginas.
			for (Map.Entry<String, ServersDAO> server: servers.entrySet()) {
				sc.append("<h1>" + server.getKey()+"</h1>");
				sc.append(server.getValue().getXSD());
			}
			content = sc.toString();
			if (content.isEmpty()) {
				status = HTTPResponseStatus.S404;	
			}
			type = MIME.TEXT_HTML.getMime();
		}
		else {
			type = MIME.APPLICATION_XML.getMime();
			status = HTTPResponseStatus.S200;
			// Comprueba si existe la página en el servidor.
			if (XsdDao.containsPage(uuid)) {	
				content = XsdDao.getXsdPage(uuid);		
			} else {
				for (Map.Entry<String, ServersDAO> server: servers.entrySet()) {
					content = server.getValue().xsdContent(uuid);
					if (content != null)
						break;
				}
				if (content == null) {
					status = HTTPResponseStatus.S404;	
				}	
			}
		}
	}

	/**
	 * Recibe una petición POST y genera una respuesta positiva o un error 400.
	 * @param request Petición HTTP.
	 */
	public void methodPost(HTTPRequest request){	
		params = request.getResourceParameters();				
		UUID randomUuid = UUID.randomUUID();
		uuid = randomUuid.toString();

		// Comprueba si el parámetro del formulario se llama xsd.
		if(params.containsKey("xsd")){
			XsdDao.createXsdPage(uuid, params.get("xsd")); // Crea la página.
			status = HTTPResponseStatus.S200;
			content = "<a href=\"xsd?uuid="+ uuid +"\">"+ uuid +"</a>";
			type = MIME.APPLICATION_XML.getMime();
		} else{
			status = HTTPResponseStatus.S400;	
			type = MIME.APPLICATION_XML.getMime();
		}
	}

	/**
	 * Recibe una petición DELETE y genera una respuesta positiva o un error 404.
	 * @param request Petición HTTP.
	 */
	public void methodDelete(HTTPRequest request){
		params = request.getResourceParameters();
		uuid = params.get("uuid");

		// Comprueba si existe la página en el servidor.
		if(XsdDao.containsPage(uuid)){
			XsdDao.deleteXsdPage(uuid); // Borra la página.
			status = HTTPResponseStatus.S200;
			type = MIME.APPLICATION_XML.getMime();
		} else{
			status = HTTPResponseStatus.S404;
			type = MIME.APPLICATION_XML.getMime();
		}
	}
	
	public HTTPResponseStatus getStatus() {
		return status;
	}

	public String getContent() {
		return content;
	}

	public String getType() {
		return type;
	}
}
