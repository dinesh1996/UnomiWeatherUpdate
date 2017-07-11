/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.unomi.samples.trainingdinesh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by dsalhotra on 27/06/2017.
 */
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static JsonNode doGetHttp(CloseableHttpClient request, String url) {
        JsonNode jsonNode = null;


        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = request.execute(httpGet);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {


            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        HttpEntity entity = response.getEntity();

        String responseString = null;
        try {
            responseString = EntityUtils.toString(entity);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }


       //logger.info(responseString);

        ObjectMapper objectMapper = new ObjectMapper();


        try {
            jsonNode = objectMapper.readTree(responseString);


        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return jsonNode;

    }

    public static CloseableHttpClient initHttpClient() {


        return HttpClients.createDefault();


    }

}





