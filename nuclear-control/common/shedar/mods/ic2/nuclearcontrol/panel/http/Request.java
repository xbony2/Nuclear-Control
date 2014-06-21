package shedar.mods.ic2.nuclearcontrol.panel.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import argo.format.CompactJsonFormatter;
import argo.format.JsonFormatter;
import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;
import argo.jdom.JsonObjectNodeBuilder;

public class Request implements Runnable{

    
    private static final JsonFormatter JSON_FORMATTER = new CompactJsonFormatter();
    private final URL url;
    @SuppressWarnings("rawtypes")
    private final ConcurrentHashMap<Long, JsonNodeBuilder> unsent;

    @SuppressWarnings("rawtypes")
    public Request(URL url, ConcurrentHashMap<Long, JsonNodeBuilder> unsent) {
        this.url = url;
        this.unsent = unsent;
    }
    
    private String formatData()
    {
        JsonObjectNodeBuilder builder = JsonNodeBuilders.anObjectBuilder();
        JsonArrayNodeBuilder array = JsonNodeBuilders.anArrayBuilder();
        List<Long> list = new ArrayList<Long>(unsent.keySet());
        for (Long key : list)
        {
            array.withElement(unsent.get(key));
            unsent.remove(key);
        }
        builder.withField("key", JsonNodeBuilders.aStringBuilder(IC2NuclearControl.instance.httpSensorKey))
                .withField("data", array);
        return JSON_FORMATTER.format(builder.build());
    }

    public void run() {
        HttpURLConnection connection;
        try
        {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            boolean isIdRequest = unsent == null;
            if(isIdRequest)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                line = reader.readLine();
                if(line!=null)
                {
                    HttpCardSender.instance.availableIds.add(Long.parseLong(line.trim()));
                    
                }
                reader.close();
            }
            else
            {
                String data = formatData();
                byte[] bytes = data.getBytes("UTF-8"); 
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setRequestProperty("Content-Length", ""+ bytes.length);
                DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
                outStream.write(bytes);
                outStream.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                reader.readLine();
                outStream.close();
                reader.close();
                connection.disconnect();
            }
        } catch (IOException e){}
  }
}
