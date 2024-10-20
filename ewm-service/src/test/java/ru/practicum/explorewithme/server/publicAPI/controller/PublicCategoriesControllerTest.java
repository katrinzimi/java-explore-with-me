package ru.practicum.explorewithme.server.publicAPI.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.server.JsonUtil;
import ru.practicum.explorewithme.server.dto.category.CategoryDto;
import ru.practicum.explorewithme.server.dto.mapper.CategoryMapper;
import ru.practicum.explorewithme.server.model.Category;
import ru.practicum.explorewithme.server.publicAPI.servise.categoty.PublicCategoriesService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PublicCategoriesController.class)
@Profile("test")
class PublicCategoriesControllerTest {
    Category category;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private PublicCategoriesService categoriesService;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "category");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAll() throws Exception {
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(category);
        List<CategoryDto> categories = List.of(
                categoryDto
        );
        Mockito.when(categoriesService.getAll(anyInt(), anyInt())).thenReturn(categories);


        mvc.perform(get("/categories?size=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(categories)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(categories.size()))
                .andExpect(jsonPath("$[0].id").value(categoryDto.getId()));
        Mockito.verify(categoriesService).getAll(0, 1);
    }

}