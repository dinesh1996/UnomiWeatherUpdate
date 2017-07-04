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
package org.apache.unomi.samples.trainingdinesh.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.unomi.api.Event;
import org.apache.unomi.api.Profile;
import org.apache.unomi.api.Session;
import org.apache.unomi.api.actions.Action;
import org.apache.unomi.api.actions.ActionExecutor;
import org.apache.unomi.api.services.EventService;
import org.apache.unomi.api.services.ProfileService;
import org.apache.unomi.samples.trainingdinesh.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Increments the number of times the user associated with the profile tweeted.
 */
public class UpdateMeteoAction implements ActionExecutor {

    private static Logger logger = LoggerFactory.getLogger(UpdateMeteoAction.class);

    private CloseableHttpClient httpClient;

    private ProfileService profileService;

    @Override
    public int execute(Action action, Event event) {
        Profile x = profileService.load(event.getProfileId());
        String tempString;

        if (httpClient == null) {
            httpClient = HttpUtils.initHttpClient();

        }
        Session session = event.getSession();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Double> location = (Map<String, Double>) session.getProperty("location");



        logger.info("latitude: {}, latitude: {}", location.get("lat"), location.get("lon"));


        JsonNode jsonNode = HttpUtils.doGetHttp(httpClient, "http://api.openweathermap.org/data/2.5/weather?lat=" +
                +location.get("lat") + "&lon=" + location.get("lon") + "&appid=3e66ec6001684dad10724dbddaf547e6");


        tempString = getTemp(jsonNode);
        String WindInfoSpeed = getWindSpeed(jsonNode);
        String WindInfoDirection = getWindDirection(jsonNode);
        String WeatherLike = getWeatherLike(jsonNode);


        logger.info("Passed");
        logger.info(tempString + " in the main function temp  ");
        logger.info(WindInfoSpeed + " in the main function Wind Speed NEw   ");
        logger.info(WindInfoDirection + " in the main function Wind Direction New  ");
        logger.info(WeatherLike + " in the main function   How it is ");


        filedPropreties(session, "sessionWeatherLike", WeatherLike);
        filedPropreties(session, "sessionWeatherTemp", tempString);
        filedPropreties(session, "sessionWeatherWindDirection", WindInfoDirection);
        filedPropreties(session, "sessionWeatherWindSpeed", WindInfoSpeed);

        // .setProperty("sessionWeatherTemp", tempString);
        logger.info(event.getSessionId());
        x.setProperty("weather", tempString);
        x.getProperty("weather");
        x.setProperty("firstName", "test");
        profileService.save(x);
        // TODO
        return EventService.SESSION_UPDATED;
    }

    public void setProfileService(ProfileService profileService) {
        this.profileService = profileService;
    }

    private String getTemp(JsonNode jsonNode) {
        float temp;

        if (jsonNode.has("main")) {

            String responseString;
            responseString = jsonNode.get("main").get("temp").toString();
            // logger.info(responseString + "    RespondString ");
            temp = Float.parseFloat(responseString);
            temp -= 273.15;

            int tempInt = (int) temp;
            if (temp - tempInt > 0.5) {
                tempInt++;
            }


            logger.info(tempInt + " Temps");
            return tempInt + "";
        } else {
            logger.info("api response is not good ");
        }
        return "-1";
    }


    private String getWindSpeed(JsonNode jsonNode) {

        JsonNode WindInfoSpeed;
        if (jsonNode.has("wind")) {
            WindInfoSpeed = jsonNode.get("wind").get("speed");

            float speed = Float.parseFloat(WindInfoSpeed.toString());
            speed *= 3.6;
            int speedFinal = (int) speed;

            logger.info(speedFinal + "    Json For The Wind Info");
            return speedFinal + "";

        } else {
            logger.info("api response is  not good ");
        }
        return null;
    }

    private String getWindDirection(JsonNode jsonNode) {

        JsonNode WindInfoDirection;
        if (jsonNode.has("wind")) {
            WindInfoDirection = jsonNode.get("wind").get("deg");
            String direction = "";
            float deg = Float.parseFloat(WindInfoDirection.toString());

            if (11.25 < deg && deg < 348.75) {
                direction = ("N");
                logger.info("N");

            } else if (11.25 < deg && deg < 33.75) {
                logger.info("NNE");
                direction = ("NNE");

            } else if (33.75 < deg && deg < 56.25) {
                logger.info("NE");
                direction = ("NE");

            } else if (56.25 < deg && deg < 78.75) {
                logger.info("ENE");
                direction = ("ENE");

            } else if (78.75 < deg && deg < 101.25) {
                logger.info("E");
                direction = ("E");

            } else if (101.25 < deg && deg < 123.75) {
                logger.info("ESE");
                direction = ("ESE");

            } else if (123.75 < deg && deg < 146.25) {
                logger.info("SE");
                direction = ("SE");

            } else if (146.25 < deg && deg < 168.75) {
                logger.info("SSE");
                direction = ("SSE");
            } else if (168.75 < deg && deg < 191.25) {
                logger.info("S");
                direction = ("S");

            } else if (191.25 < deg && deg < 213.75) {
                logger.info("SSW");
                direction = ("SSW");

            } else if (213.75 < deg && deg < 236.25) {
                logger.info("SW");
                direction = ("SW");

            } else if (236.25 < deg && deg < 258.75) {
                logger.info("WSW");
                direction = ("WSW");

            } else if (258.75 < deg && deg < 281.25) {
                logger.info("W");
                direction = ("W");

            } else if (281.25 < deg && deg < 303.75) {
                logger.info("WNW");
                direction = ("WNW");

            } else if (303.75 < deg && deg < 326.25) {
                logger.info("NW");
                direction = ("NW");

            } else if (326.25 < deg && deg < 348.75) {
                logger.info("NNW");
                direction = ("NNW");
            }


            logger.info(WindInfoDirection + "    Json For The Wind Info");
            return direction;

        } else {
            logger.info("api response is  not good ");
        }
        return null;
    }


    private String getWeatherLike(JsonNode jsonNode) {

        JsonNode weatherInfo;

        if (jsonNode.has("weather")) {
            weatherInfo = jsonNode.get("weather");


            //   logger.info(weatherInfo + "    Json For The Weather Like Info");
            if (weatherInfo.size() > 0) {
                weatherInfo = weatherInfo.get(0).get("main");

                return weatherInfo.asText();

            }

        } else {
            logger.info("api response is  not good ");
        }
        return "bad";
    }


    private void filedPropreties(Session session, String property, String value) {

        session.setProperty(property, value);
    }

}


