package shedar.mods.ic2.nuclearcontrol.panel.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;
import argo.jdom.JsonObjectNodeBuilder;

public class HttpCardSender
{
	private static final String ID_URL_TEMPLATE = "http://sensors.modstats.org/api/v1/register?p=";
	private static final String DATA_URL_TEMPLATE = "http://sensors.modstats.org/api/v1/report";
	public static HttpCardSender instance = new HttpCardSender();
	@SuppressWarnings("rawtypes")
	private ConcurrentHashMap<Long, JsonNodeBuilder> unsent = new ConcurrentHashMap<Long, JsonNodeBuilder>();
	public ConcurrentLinkedQueue<Long> availableIds = new ConcurrentLinkedQueue<Long>();
	private ExecutorService executor = Executors.newFixedThreadPool(2); 

	private HttpCardSender(){}

	public void requestId(){
		try{
			executor.submit(new Request(new URL(ID_URL_TEMPLATE + IC2NuclearControl.instance.httpSensorKey), null));
		}catch (MalformedURLException e){
			e.printStackTrace();
		}
	}

	public void send(){
		try{
			executor.submit(new Request(new URL(DATA_URL_TEMPLATE), unsent));
		}catch (MalformedURLException e){
			e.printStackTrace();
		}
	}

	public void add(NBTTagCompound cardData, UUID cardType, Long id){
		JsonObjectNodeBuilder builder = JsonNodeBuilders.anObjectBuilder();
		builder.withField("id", JsonNodeBuilders.aNumberBuilder(id.toString()));
		builder.withField("type", JsonNodeBuilders.aStringBuilder(cardType.toString().replace("-", "")));
		Iterator iterator = cardData.func_150296_c().iterator();
		while (iterator.hasNext()){
			String s = (String)iterator.next();
			NBTBase tag = cardData.getTag(s);
			if (!s.equals("_webSensorId")){
				if (s.equals("energyL") || s.equals("maxStorageL")){
					builder.withField(s, JsonNodeBuilders.aStringBuilder(String.valueOf(Double.valueOf(tag.toString()).longValue())));
				}else{
					builder.withField(s, JsonNodeBuilders.aStringBuilder(tag.toString()));
				}
			}
		}
		unsent.put(id, builder);
	}
}