package info.mgsolutions.tomcat;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Plain Servlet, without Spring/Memcached, responding asynchronously
 */
@WebServlet(urlPatterns = AsyncPlainTextServlet.URL_PATTERN, asyncSupported = true)
public class AsyncPlainTextServlet extends HttpServlet {

	static final String URL_PATTERN = "/testbed/asyncplaintext";
	private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";
	private static final byte[] CONTENT = "Hello world!".getBytes(StandardCharsets.UTF_8);
	private static final int CONTENT_LENGTH = CONTENT.length;

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
		final AsyncContext asyncContext = req.startAsync();
		asyncContext.setTimeout(10000L);
		asyncContext.start(() -> {
			try {
				final ServletResponse response = asyncContext.getResponse();
				response.setContentType(CONTENT_TYPE);
				response.setContentLength(CONTENT_LENGTH);
				response.getOutputStream().write(CONTENT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			asyncContext.complete();
		});
	}
}
