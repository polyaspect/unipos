package unipos.data.components.category;

import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

// Created by dominik on 29.07.15.



@RestController
@Api("/categories")
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @ApiOperation(value = "List all stored Categories",
            response = Category.class,
            responseContainer = "List")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Category> listCategories() {
        return categoryService.findAllCategories();
    }

    @ApiOperation(value = "Get ONE Category by its Mongo-DB Id",
            response = Category.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "No Category with the given Id was found in the Database")
    })
    @RequestMapping(value = "/dbId/{dbId}", method = RequestMethod.GET)
    public Category findCategoryByMongoDbId(@PathVariable("dbId") String dbId, HttpServletResponse response) {
        try {
            return categoryService.findCategoryByMongoDbId(dbId);
        } catch (IllegalArgumentException e) {
            response.setStatus(404);
            return null;
        }
    }

    @ApiOperation(value = "Find Categories by their Name",
        response = Category.class,
        responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 404, message = "No Category with the given Name was found in the DB")
    })
    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public List<Category> findCategoryByName(@PathVariable("name") String name, HttpServletResponse response) {
        try {
            return categoryService.findCategoryByName(name);
        } catch (IllegalArgumentException e) {
            response.setStatus(404);
            return null;
        }
    }

    @ApiOperation("Delete a Category by its Mongo-Db Id")
    @ApiResponses({
            @ApiResponse(code = 404, message = "No Entity with the given Id is stored in the Database")
    })
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void deleteCategoryByMongoDbId(@RequestParam("id") String id ,HttpServletResponse response) {
        try {
            categoryService.deleteCategoryByMongoId(id);
        } catch (IllegalArgumentException e) {
            response.setStatus(404);
        }
    }

    @RequestMapping(value = "/guid" ,method = RequestMethod.DELETE)
    public void deleteCategoryByGUID(@RequestParam("guid") String guid, HttpServletResponse response) {
        if(guid != null && !guid.isEmpty()) {
            categoryService.deleteByGUID(guid);
        } else {
            response.setStatus(400);
        }
    }

    @ApiOperation("Save a new Category")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Category not saved in the Database")
    })
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Category saveCategory(@ApiParam @RequestBody Category category, HttpServletResponse response) {
        try {
            if(category.getId() != null && !category.getId().isEmpty()) {
                categoryService.updateCategory(category);
                response.setStatus(200);
                return category;
            }
            Category category1 = categoryService.saveCategory(category);
            if(category1 != null) {
                response.setStatus(200);
                return category1;
            } else {
                response.setStatus(400);
                return null;
            }
        } catch (IllegalArgumentException e) {
            response.setStatus(400);
            return null;
        }
    }

}
