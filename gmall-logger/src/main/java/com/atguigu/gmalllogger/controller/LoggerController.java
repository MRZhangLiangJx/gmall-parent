package com.atguigu.gmalllogger.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.GmallConstants;
import com.sun.net.httpserver.Authenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.midi.Soundbank;

@RestController
@Slf4j
public class LoggerController {

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @GetMapping("test")
    public String getTest(@RequestParam String testString){
        return testString;
    }

    @PostMapping("log")
    public String getLog(@RequestParam String logString){

        JSONObject logJson = JSON.parseObject(logString);
        logJson.put("ct",System.currentTimeMillis());
        String jsonString = logJson.toJSONString();
        //发送到kafka
        if ("startup".equals(logJson.getString("type"))){

            kafkaTemplate.send(GmallConstants.KAFKA_TOPIC_STARTUP,jsonString);
        }else if("event".equals(logJson.getString("type"))){

            kafkaTemplate.send(GmallConstants.KAFKA_TOPIC_EVENT,jsonString);
        }
//        log.info(jsonString);

        return "success";
    }
}
