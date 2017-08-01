package fpcc.protocol;

public class Message {
	private String statusLine;
	private String headers;
	private String content;

	// Codigos de estado para utilizacao
	// em lado servidor
	public static final String OK = "200 OK";
	public static final String NOT_FOUND = "404 NOT FOUND";

	public static final String HTTP1_1 = "HTTP/1.1";

	// Constante para representar o tamanho do pacote
	public static final int DATA_LENGTH = Integer.BYTES + 1;

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public Message() {
		statusLine = "";
		headers = "";
		content = "";
	}

	public void setStatus(String status) {
		statusLine = HTTP1_1 + " " + status;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void appendContent(String content) {
		this.content += content;
	}

	public void addHeadersField(String headerLine) {
		headers += headerLine + LINE_SEPARATOR;
	}

	public String getHeadersField() {
		return headers;
	}

	@Override
	public String toString() {
		//
		return statusLine + LINE_SEPARATOR + headers + ("Content-Length: " + content.getBytes().length + LINE_SEPARATOR)
				+ LINE_SEPARATOR + content;
	}

	public boolean isEmpty() {
		return headers.isEmpty() && content.isEmpty();
	}

}
