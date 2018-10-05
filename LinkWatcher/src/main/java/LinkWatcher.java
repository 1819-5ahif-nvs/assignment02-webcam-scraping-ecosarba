import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LinkWatcher implements HttpHandler {

    private static Logger logger = Logger.getLogger("WatchLogger");
    private static FileHandler fh;

    static final int PORT = 8080;

    // verbose mode
    static final boolean verbose = true;

    // Client Connection via Socket Class
    private Socket connect;


    public static void main(String[] args){

        try {

            // This block configure the logger with handler and formatter
            fh = new FileHandler("WatchLogger.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages
            logger.info("init log");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);

            server.createContext("/", new LinkWatcher());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = "<video controls>\n" +
                "  <source src=\"" + scrap() + "\" type=\"video/mp4\">\n" +
                "</video>";
        Headers h = t.getResponseHeaders();
        h.set("Content-Type","text/html");
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static String scrap(){
        String src;
        Document doc = null;
        Element vid = null;


        try{

            doc = Jsoup.connect("https://webtv.feratel.com/webtv/?cam=5132&design=v3&c0=0&c2=1&lg=en&s=0")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .get();
            vid = doc.getElementById("fer_video");
            src = vid.select("source").first().attr("src");
            logger.info(src);
            return src;
        }catch (IOException e){
            e.printStackTrace();
        }

        return "notfound";
    }

}
