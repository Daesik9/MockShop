package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.mockshop.dto.ItemCreationDto;
import project.mockshop.dto.ItemDto;
import project.mockshop.dto.ItemSearchCondition;
import project.mockshop.dto.ItemThumbDto;
import project.mockshop.response.Response;
import project.mockshop.service.ItemService;
import project.mockshop.util.FileStore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final FileStore fileStore;

    @PostMapping("/items")
    public Response createItem(@ModelAttribute ItemCreationDto itemCreationDto) throws IOException {
        Long itemId = itemService.createItem(itemCreationDto);
        return Response.success(itemId);
    }

    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("/items/search/{name}")
    public Response searchItems(@PathVariable String name) {
        List<ItemDto> items = itemService.findItemsByNameLike(name);

        return Response.success(HttpStatus.OK.value(), items);
    }

    @GetMapping("/items/search")
    public Response search(ItemSearchCondition searchCondition,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemThumbDto> result = itemService.search(searchCondition, pageable);
        return Response.success(HttpStatus.OK.value(), result);
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

    @GetMapping("/merchants/{merchantId}/items")
    public Response getItemsByMerchant(@PathVariable Long merchantId) {
        List<ItemDto> itemsByMerchantId = itemService.findItemsByMerchantId(merchantId);
        System.out.println("itemsByMerchantId = " + itemsByMerchantId);
        return Response.success(itemsByMerchantId);
    }
}
