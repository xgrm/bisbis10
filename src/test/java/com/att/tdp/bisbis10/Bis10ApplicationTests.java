package com.att.tdp.bisbis10;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestaurantManagementSystemTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	private String getBaseUrl() {
		return "http://localhost:" + port;
	}
	private void checkRestaurantFormat(Map<String, Object> restaurant){
		restaurant.containsKey("id");
		assertTrue((Boolean) restaurant.containsKey("id"));
		assertTrue((Boolean) restaurant.containsKey("name"));
		assertTrue((Boolean) restaurant.containsKey("averageRating"));
		assertTrue((Boolean) restaurant.containsKey("isKosher"));
		assertTrue((Boolean) restaurant.containsKey("cuisines"));
		assertTrue(restaurant.get("cuisines") instanceof List);
		List<String> cuisines = (List<String>) restaurant.get("cuisines");
	}
	private void checkRestaurantFormatById(Map<String, Object> restaurant){
		checkRestaurantFormat(restaurant);
		assertTrue((Boolean) restaurant.containsKey("dishes"));
		checkDishesFormat(restaurant.get("dishes"));
	}
	private void checkDishesFormat(Object dishes){
		assertTrue(dishes instanceof List);
		for(Object dish: (List)dishes) {
			Map<String, Object> mapDish = (Map<String, Object>)dish;
			assertTrue( mapDish.containsKey("id"));
			assertTrue( mapDish.containsKey("name"));
			assertTrue( mapDish.containsKey("description"));
			assertTrue( mapDish.containsKey("price"));
		}
	}
	private Map<String, Object> getFirstRestaurant(boolean first){
		ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/restaurants", String.class);
		List<Map<String, Object>> restaurants = null;
		try {
			restaurants = objectMapper.readValue(response.getBody(), new TypeReference<List<Map<String, Object>>>() {});
			return restaurants.get(first ? 0: (restaurants.size()>1?restaurants.size()-1:0));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	private Map<String, Object> getFirstRestaurant(){
		return getFirstRestaurant(true);
	}
	private Map<String, Object> getRestaurantById(String id){
		String restaurantId = id;
		ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/restaurants/" + restaurantId, String.class);
		Map<String, Object> restaurant = null;
		try {
			return objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	private Optional<Map<String, Object>> getFirstDish(){
		ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/restaurants", String.class);
		List<Map<String, Object>> restaurants = null;
		try {
			restaurants = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
			for(Map<String, Object> restaurant :restaurants){
				Map<String,Object> restaurantWithDishes = getRestaurantById(restaurant.get("id").toString());
				List<Map<String, Object>> dishes = (List<Map<String, Object>>)restaurantWithDishes.get("dishes");
				if(!dishes.isEmpty()) {
					Map<String, Object> dish =dishes.get(0);
					dish.put("restaurantId",restaurant.get("id").toString());
					return Optional.of(dish);
				}
			}
			return Optional.empty();
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	@Test
	void testAddRestaurant() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("name", "New Restaurant");
		personJsonObject.put("isKosher", false);
		personJsonObject.put("cuisines", Arrays.asList("Italian"));
		HttpEntity<JSONObject> requestEntity = new HttpEntity<>(personJsonObject, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/restaurants", requestEntity, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		restTemplate.postForEntity("/restaurants", requestEntity, String.class);
	}

	@Test
	void testGetAllRestaurants() {
		ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/restaurants", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<Map<String, Object>> restaurants = null;
		try {
			restaurants = objectMapper.readValue(response.getBody(), new TypeReference<List<Map<String, Object>>>() {});
			assertTrue((Boolean) !restaurants.isEmpty());
			for(Map<String, Object> restaurant :restaurants) {
				checkRestaurantFormat(restaurant);
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

	}

	@Test
	void testGetRestaurantsByCuisine() {
		String cuisine = "Italian";
		ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/restaurants?cuisine=" + cuisine, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<Map<String, Object>> restaurants = null;
		try {
			restaurants = objectMapper.readValue(response.getBody(), new TypeReference<List<Map<String, Object>>>() {});
			assertTrue((Boolean) !restaurants.isEmpty());
			for(Map<String, Object> restaurant :restaurants) {
				checkRestaurantFormat(restaurant);
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void testGetRestaurantById() {
		String restaurantId = getFirstRestaurant().get("id").toString();
		ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/restaurants/" + restaurantId, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Map<String, Object> restaurant = null;
		try {
			restaurant = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
			checkRestaurantFormatById(restaurant);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void testUpdateRestaurant() {

		String restaurantId = getFirstRestaurant().get("id").toString();
		JSONObject requestBody = new JSONObject();
		requestBody.put("cuisines", Collections.singletonList("Japanese"));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				getBaseUrl() + "/restaurants/" + restaurantId,
				HttpMethod.PUT,
				requestEntity,
				String.class
		);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Map<String, Object> updatedRestaurant = getRestaurantById(restaurantId);
		assertEquals(updatedRestaurant.get("cuisines").toString(),requestBody.get("cuisines").toString());
	}

	@Test
	void testAddRating() {
		String restaurantId = getFirstRestaurant().get("id").toString();
		JSONObject requestBody = new JSONObject();
		requestBody.put("restaurantId", restaurantId);
		requestBody.put("rating", "3.3");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl() + "/ratings", requestEntity, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test

	void testAddDish() {
		String restaurantId = getFirstRestaurant(false).get("id").toString();
		JSONObject requestBody = new JSONObject();
		requestBody.put("name", "New Dish");
		requestBody.put("description", "Delicious new dish");
		requestBody.put("price", 20);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl() + "/restaurants/" + restaurantId + "/dishes", requestEntity, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test

	void testUpdateDish() {
		Map<String, Object> dish = getFirstDish().orElse(getFirstDish().orElseThrow());
		String restaurantId = dish.get("restaurantId").toString();
		String dishId = dish.get("id").toString();

		JSONObject requestBody = new JSONObject();
		requestBody.put("description", "Updated description");
		requestBody.put("price", 30);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				getBaseUrl() + "/restaurants/" + restaurantId + "/dishes/" + dishId,
				HttpMethod.PUT,
				requestEntity,
				String.class
		);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	@Order(10)
	void testGetDishesByRestaurant() {
		Map<String, Object> dish = getFirstDish().orElse(getFirstDish().orElseThrow());
		String restaurantId = dish.get("restaurantId").toString();
		ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/restaurants/" + restaurantId + "/dishes", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<Map<String, Object>> dishes = null;
		try {
			dishes = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
			checkDishesFormat(dishes);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Test

	void testPlaceOrder() {
		Map<String, Object> dish = getFirstDish().orElse(getFirstDish().orElseThrow());
		String restaurantId = dish.get("restaurantId").toString();
		String dishId = dish.get("id").toString();

		JSONObject requestBody = new JSONObject();
		requestBody.put("restaurantId",restaurantId);


		JSONObject orderItem1 = new JSONObject();
		orderItem1.put("dishId", dishId);
		orderItem1.put("amount", 1);
		requestBody.put("orderItems", new Object[]{orderItem1});

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl() + "/order", requestEntity, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<Map<String, Object>> responseMap = null;
		try {
			responseMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
			assertTrue(UUID.fromString(responseMap.get(0).get("orderId").toString()) instanceof UUID);
		} catch (JsonProcessingException e) {
			assertFalse(false);
		}
		catch (IllegalArgumentException e){
			assertTrue(false);
		}
	}


	@Test
	@Order(11)
	void testDeleteDish() {
		Map<String, Object> dish = getFirstDish().orElse(getFirstDish().orElseThrow());
		String restaurantId = dish.get("restaurantId").toString();
		String dishId = dish.get("id").toString();


		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(
				getBaseUrl() + "/restaurants/" + restaurantId + "/dishes/" + dishId,
				HttpMethod.DELETE,
				requestEntity,
				String.class
		);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	@Order(12)
	@Test
	void testDeleteRestaurant() {
		String restaurantId = getFirstRestaurant().get("id").toString();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(
				getBaseUrl() + "/restaurants/" + restaurantId,
				HttpMethod.DELETE,
				requestEntity,
				String.class
		);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
}
