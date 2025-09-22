package com.hipotec.simulator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RequestSchedulePaginationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Test pagination with default parameters")
    void testDefaultPagination() throws Exception {
        String url = "http://localhost:" + port + "/api?principal=100000&interest=5.5&duration=24";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCode().value());
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        
        // Verify pagination metadata
        assertEquals(0, jsonNode.get("number").asInt(), "Should be page 0");
        assertEquals(20, jsonNode.get("size").asInt(), "Default size should be 20");
        assertEquals(24 * 12, jsonNode.get("totalElements").asInt(), "Should have 24 total elements");
        assertEquals(15, jsonNode.get("totalPages").asInt(), "Should have 15 pages with size 20");
        assertTrue(jsonNode.get("first").asBoolean(), "Should be first page");
        assertFalse(jsonNode.get("last").asBoolean(), "Should not be last page");
        assertEquals(20, jsonNode.get("numberOfElements").asInt(), "Should have 20 elements on first page");
        
        // Verify content structure
        assertTrue(jsonNode.has("content"), "Should have content field");
        assertEquals(20, jsonNode.get("content").size(), "Content should have 20 items");
    }

    @Test
    @DisplayName("Test pagination with custom page size")
    void testCustomPageSize() throws Exception {
        String url = "http://localhost:" + port + "/api?principal=100000&interest=5.5&duration=24&page=0&size=5";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCode().value());
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        
        // Verify pagination metadata
        assertEquals(0, jsonNode.get("number").asInt(), "Should be page 0");
        assertEquals(5, jsonNode.get("size").asInt(), "Size should be 5");
        assertEquals(24 * 12, jsonNode.get("totalElements").asInt(), "Should have 24 total elements");
        assertEquals(58, jsonNode.get("totalPages").asInt(), "Should have 58 pages with size 5");
        assertTrue(jsonNode.get("first").asBoolean(), "Should be first page");
        assertFalse(jsonNode.get("last").asBoolean(), "Should not be last page");
        assertEquals(5, jsonNode.get("numberOfElements").asInt(), "Should have 5 elements");
        
        // Verify content
        assertEquals(5, jsonNode.get("content").size(), "Content should have 5 items");
    }

    @Test
    @DisplayName("Test last page with partial content")
    void testLastPagePartialContent() throws Exception {
        // 288 elements with size 5 = 58 pages (0-57), last page (57) has 3 elements
        String url = "http://localhost:" + port + "/api?principal=100000&interest=5.5&duration=24&page=57&size=5";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCode().value());
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        
        // Verify pagination metadata for last page
        assertEquals(57, jsonNode.get("number").asInt(), "Should be page 57");
        assertEquals(5, jsonNode.get("size").asInt(), "Size should be 5");
        assertEquals(24 * 12, jsonNode.get("totalElements").asInt(), "Should have 288 total elements");
        assertEquals(58, jsonNode.get("totalPages").asInt(), "Should have 58 pages total");
        assertFalse(jsonNode.get("first").asBoolean(), "Should not be first page");
        assertTrue(jsonNode.get("last").asBoolean(), "Should be last page");
        assertEquals(3, jsonNode.get("numberOfElements").asInt(), "Should have 3 elements on last page");
        
        // Verify content
        assertEquals(3, jsonNode.get("content").size(), "Content should have 3 items (last page)");
    }

    @Test
    @DisplayName("Test middle page navigation")
    void testMiddlePageNavigation() throws Exception {
        String url = "http://localhost:" + port + "/api?principal=100000&interest=5.5&duration=24&page=2&size=5";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCode().value());
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        
        // Verify pagination metadata for middle page
        assertEquals(2, jsonNode.get("number").asInt(), "Should be page 2");
        assertEquals(5, jsonNode.get("size").asInt(), "Size should be 5");
        assertEquals(24 * 12, jsonNode.get("totalElements").asInt(), "Should have 24 total elements");
        assertEquals(58, jsonNode.get("totalPages").asInt(), "Should have 58 pages total");
        assertFalse(jsonNode.get("first").asBoolean(), "Should not be first page");
        assertFalse(jsonNode.get("last").asBoolean(), "Should not be last page");
        assertEquals(5, jsonNode.get("numberOfElements").asInt(), "Should have 5 elements");
        
        // Verify content
        assertEquals(5, jsonNode.get("content").size(), "Content should have 5 items");
    }

    @Test
    @DisplayName("Test page beyond bounds returns empty content")
    void testPageBeyondBounds() throws Exception {
        String url = "http://localhost:" + port + "/api?principal=100000&interest=5.5&duration=24&page=58&size=5";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCode().value());
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        
        // Verify pagination metadata for out-of-bounds page
        assertEquals(58, jsonNode.get("number").asInt(), "Should be page 58");
        assertEquals(5, jsonNode.get("size").asInt(), "Size should be 5");
        assertEquals(24 * 12, jsonNode.get("totalElements").asInt(), "Should have 24 total elements");
        assertEquals(58, jsonNode.get("totalPages").asInt(), "Should have 58 pages total");
        assertFalse(jsonNode.get("first").asBoolean(), "Should not be first page");
        assertTrue(jsonNode.get("last").asBoolean(), "Should be considered last page");
        assertEquals(0, jsonNode.get("numberOfElements").asInt(), "Should have 0 elements");
        
        // Verify empty content
        assertEquals(0, jsonNode.get("content").size(), "Content should be empty");
    }

    @Test
    @DisplayName("Test content data integrity in pagination")
    void testContentDataIntegrity() throws Exception {
        String url = "http://localhost:" + port + "/api?principal=100000&interest=5.5&duration=24&page=0&size=5";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCode().value());
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        JsonNode content = jsonNode.get("content");
        
        // Verify first period data structure
        JsonNode firstPeriod = content.get(0);
        assertTrue(firstPeriod.has("id"), "Period should have id");
        assertTrue(firstPeriod.has("principal"), "Period should have principal");
        assertTrue(firstPeriod.has("interest"), "Period should have interest");
        assertTrue(firstPeriod.has("restante"), "Period should have restante");
        assertTrue(firstPeriod.has("capitalPendiente"), "Period should have capitalPendiente");
        
        // Verify data types and reasonable values
        assertTrue(firstPeriod.get("id").isInt(), "ID should be integer");
        assertTrue(firstPeriod.get("principal").isDouble(), "Principal should be double");
        assertTrue(firstPeriod.get("interest").isDouble(), "Interest should be double");
        assertTrue(firstPeriod.get("principal").asDouble() > 0, "Principal should be positive");
        assertTrue(firstPeriod.get("interest").asDouble() > 0, "Interest should be positive");
    }
}
