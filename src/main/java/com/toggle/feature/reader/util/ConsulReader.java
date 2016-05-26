package com.toggle.feature.reader.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;

@Component
public class ConsulReader {

    @Autowired
    private ConsulClient consul;

    private Map<String, String> map = new ConcurrentHashMap<String, String>();

    @Scheduled(fixedDelayString = "${consul.scheduled.delay}")
    public void readerSchedule() {
	try {
	    map.clear();

	    Response<List<GetValue>> response = consul.getKVValues("");
	    List<GetValue> list = response.getValue();
	    System.out.print("Key : ");
	    for (GetValue value : list) {
		byte[] valueArray = Base64.decodeBase64(value.getValue());
		map.put(value.getKey(), new String(valueArray));
	    }

	    for (Iterator<String> iteratorKey = map.keySet().iterator(); iteratorKey.hasNext();) {
		String key = iteratorKey.next();
		System.out.print(key + "=" + map.get(key) + ",");
	    }
	} catch (Exception e) {
	    System.out.println("Can't read key "+e.getClass());
	}finally{
	    System.out.println();
	}
    }

    public String get(String key) {
	return map.get(key);
    }
}
