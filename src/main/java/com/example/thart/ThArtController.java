package com.example.thart;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@Controller
public class ThArtController {
    private int yOffset = 20;
    private String currentWord = "";
    private boolean justEntered = false;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @MessageMapping("/move")
    @SendTo("/topic/position")
    public Integer move(String direction) {
	System.out.println(direction);
        if ("up".equals(direction) && yOffset > 0) yOffset--;
        if ("down".equals(direction) && yOffset < 40) yOffset++;
        return yOffset;
    }

    @MessageMapping("/type")
    @SendTo("/topic/typed")
    public String type(String json) throws com.fasterxml.jackson.core.JsonProcessingException {
	System.out.println("Received: " + json);
	ObjectMapper mapper = new ObjectMapper();
	Map<String, Object> map = mapper.readValue(json, Map.class);

	if (Boolean.TRUE.equals(map.get("submit"))) {
	    String myWord = (String) (map.get("word"));
	    System.out.println("Word: " + myWord);
	    return myWord;
	}
	return null; // ignore all other keys
    }
}
