package com.product;

import com.product.domain.Product;
import com.product.repository.ProductRepository;
import com.product.util.Util;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class ProductControllerTest extends BaseTest {

    public static final String API_PRODUCT = "/api/products";

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void createProduct() throws Exception {
        //given
        Product product = new Product(TEST_UUID, "testProduct", 10.23, DUMMY_DATE);

        //when
        this.mockMvc.perform(post(API_PRODUCT + "/create")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(Util.jsonString(product)))

            //then
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk());
    }

    @Test
    public void getProductTest() throws Exception {
        //given
        Product product = new Product(TEST_UUID, "testProduct", 10.23, DUMMY_DATE);
        productRepository.save(product);


        //when
        MvcResult mvcResult = this.mockMvc.perform(get(API_PRODUCT + "/{productId}", TEST_UUID)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE))

            //then
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk()).andReturn();

        Product response = Util.parseJson(mvcResult.getResponse().getContentAsString(), Product.class);
        Assert.assertEquals(response.getId(), TEST_UUID);
    }

    @Test
    public void getProductsTest() throws Exception {
        //given
        Product product = new Product(TEST_UUID, "testProduct", 10.23, DUMMY_DATE);
        productRepository.save(product);

        Product product2 = new Product(UUID.randomUUID(), "testProduct", 10.23, DUMMY_DATE);
        productRepository.save(product2);


        //when
        this.mockMvc.perform(get(API_PRODUCT)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE))

            //then
            .andExpect(jsonPath("$", Matchers.hasSize(2)))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk());
    }

    @Test
    public void deleteCommentTest() throws Exception {
        //given
        Product product = new Product(TEST_UUID, "testProduct", 10.23, DUMMY_DATE);
        productRepository.save(product);

        this.mockMvc.perform(delete(API_PRODUCT + "/{productId}", TEST_UUID))

            //then
            .andExpect(status().isOk());

        Assert.assertFalse(productRepository.findByid(TEST_UUID).isPresent());
    }

}
