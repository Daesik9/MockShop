package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.mockshop.dto.ItemCreationDto;
import project.mockshop.dto.ItemDto;
import project.mockshop.dto.ItemSearchCondition;
import project.mockshop.entity.UploadFile;
import project.mockshop.response.Response;
import project.mockshop.service.ItemService;
import project.mockshop.util.FileStore;

import java.io.IOException;
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
        //TODO: 너무 복잡한데... 나중에 수정하기
        if (itemCreationDto.getThumbnail() != null) {
            UploadFile thumbnail = fileStore.createUploadFile(itemCreationDto.getThumbnail());
            fileStore.storeFile(itemCreationDto.getThumbnail(), thumbnail);
        }
        if (itemCreationDto.getDescriptionImg1() != null) {
            UploadFile descriptionImg1 = fileStore.createUploadFile(itemCreationDto.getDescriptionImg1());
            fileStore.storeFile(itemCreationDto.getDescriptionImg1(), descriptionImg1);
        }
        if (itemCreationDto.getDescriptionImg2() != null) {
            UploadFile descriptionImg2 = fileStore.createUploadFile(itemCreationDto.getDescriptionImg2());
            fileStore.storeFile(itemCreationDto.getDescriptionImg2(), descriptionImg2);
        }
        if (itemCreationDto.getDescriptionImg3() != null) {
            UploadFile descriptionImg3 = fileStore.createUploadFile(itemCreationDto.getDescriptionImg3());
            fileStore.storeFile(itemCreationDto.getDescriptionImg3(), descriptionImg3);
        }

        Long itemId = itemService.createItem(itemCreationDto);
        return Response.success(itemId);
    }

    @GetMapping("/items/search/{name}")
    public Response searchItems(@PathVariable String name) {
        List<ItemDto> items = itemService.findItemsByNameLike(name);

        return Response.success(HttpStatus.OK.value(), items);
    }

    @GetMapping("/items/search")
    public Response search(ItemSearchCondition searchCondition,
                           @RequestParam int page,
                           @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemDto> result = itemService.search(searchCondition, pageable);
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
}
