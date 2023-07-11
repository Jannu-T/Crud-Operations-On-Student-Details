import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter("/*")
public class LoggingFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        PrintWriter out=response.getWriter();
        // Pre-processing logic
        out.write("Filter: Before executing the request\n");

        // Pass the request through the filter chain
        chain.doFilter(request, response);

        // Post-processing logic
        out.write("\nFilter: After executing the request");
    }

    public void destroy() {

    }
}

