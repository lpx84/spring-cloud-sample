package com.xiang.cloud.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class XiangProviderApplicationTests {

	@Test
	public void contextLoads() throws IOException {
		OkHttpClient client = new OkHttpClient.Builder().build();

		Headers headers = Headers.of("Content-Type", "application/json");

		for (int i = 32150; i > 31150; -- i) {
			Response response = client.newCall(new Request.Builder()
					.post(RequestBody.create(MediaType.parse("application/json"), "{  \"data\": {    \"callingUser\": \"account-opening\",    \"referenceId\": \"" + i + "\"  }}"))
					.url("http://localhost:8583/internal/v1/aml/cdd/result")
					.build()).execute();
			String str = response.body().string();
			JSONObject obj = JSON.parseObject(str);
			if (obj != null && obj.getJSONObject("data") != null && StringUtils.isNotBlank(obj.getJSONObject("data").getString("caseId"))) {
				System.out.println(str);
			} else {
				System.out.println("0");
			}
		}
	}

}
