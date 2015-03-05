package shedar.mods.ic2.nuclearcontrol.panel.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;
import argo.jdom.JsonObjectNodeBuilder;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;

public class HttpCardSender {
	private static final String ID_URL_TEMPLATE = "http://sensors.modstats.org/api/v1/register?p=";
	private static final String DATA_URL_TEMPLATE = "http://sensors.modstats.org/api/v1/report";
	public static HttpCardSender instance = new HttpCardSender();

	@SuppressWarnings("rawtypes")
	private final ConcurrentHashMap<Long, JsonNodeBuilder> unsent = new ConcurrentHashMap<Long, JsonNodeBuilder>();
	public ConcurrentLinkedQueue<Long> availableIds = new ConcurrentLinkedQueue<Long>();
	// single thread executor service with a maximum queue size of 64 elements
	private final ExecutorService executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(64));

	private HttpCardSender() {}

	public void requestId() {
		try {
			executor.submit(new Request(new URL(ID_URL_TEMPLATE + IC2NuclearControl.instance.httpSensorKey), null));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RejectedExecutionException e) {
			// the server isn't processing requests fast enough, drop the request
			// TODO: reschedule? fallback?
		}
	}

	public void send() {
		try {
			executor.submit(new Request(new URL(DATA_URL_TEMPLATE), unsent));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RejectedExecutionException e) {
			// the server isn't processing requests fast enough, drop the request
			// TODO: reschedule? fallback?
		}
	}

	public void add(NBTTagCompound cardData, UUID cardType, Long id) {
		JsonObjectNodeBuilder builder = JsonNodeBuilders.anObjectBuilder();
		builder.withField("id", JsonNodeBuilders.aNumberBuilder(id.toString()));
		builder.withField("type", JsonNodeBuilders.aStringBuilder(cardType.toString().replace("-", "")));
		Iterator iterator = cardData.func_150296_c().iterator();
		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			NBTBase tag = cardData.getTag(s);
			if (!s.equals("_webSensorId")) {
				if (s.equals("energyL") || s.equals("maxStorageL")) {
					builder.withField(s, JsonNodeBuilders.aStringBuilder(String.valueOf(Double.valueOf(tag.toString()).longValue())));
				} else {
					builder.withField(s, JsonNodeBuilders.aStringBuilder(tag.toString()));
				}
			}
		}
		unsent.put(id, builder);
	}
}