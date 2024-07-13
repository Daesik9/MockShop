package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.mockshop.dto.ItemDto;
import project.mockshop.response.Response;
import project.mockshop.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/search/{name}")
    public Response searchItems(@PathVariable String name) {
        List<ItemDto> items = itemService.findItemsByNameLike(name);

        return Response.success(HttpStatus.OK.value(), items);
    }

    @GetMapping("/items/{id}")
    public Response itemDetail(@PathVariable Long id) {
        ItemDto item = itemService.findById(id);

        return Response.success(HttpStatus.OK.value(), item);
    }

    @GetMapping("/items/best-five")
    public Response getBestFiveThisWeek() {
        List<ItemDto> bestFiveThisWeek = itemService.findBestFiveThisWeek();

        return Response.success(bestFiveThisWeek);
    }
}
