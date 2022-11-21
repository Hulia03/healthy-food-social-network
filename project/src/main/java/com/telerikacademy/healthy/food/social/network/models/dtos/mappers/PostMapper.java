package com.telerikacademy.healthy.food.social.network.models.dtos.mappers;

import com.telerikacademy.healthy.food.social.network.models.Category;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.dtos.posts.PostDto;
import com.telerikacademy.healthy.food.social.network.services.contracts.CategoriesService;
import com.telerikacademy.healthy.food.social.network.services.contracts.VisibilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

import static com.telerikacademy.healthy.food.social.network.models.dtos.mappers.Mapper.getNotNull;

@Component
public class PostMapper {
    private static final int WIDTH = 800;
    private final VisibilitiesService visibilitiesService;
    private final CategoriesService categoriesService;

    private final MediaMapper mediaMapper;

    @Autowired
    public PostMapper(VisibilitiesService visibilitiesService,
                      CategoriesService categoriesService,
                      MediaMapper mediaMapper) {
        this.visibilitiesService = visibilitiesService;
        this.categoriesService = categoriesService;
        this.mediaMapper = mediaMapper;
    }

    public Post toPost(PostDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setTimestamp(new Timestamp(System.currentTimeMillis()));
        post.setVisibility(visibilitiesService.getVisibilityById(dto.getVisibilityId()));
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            post.setMedia(mediaMapper.toMedia(dto.getFile(), dto.getVisibilityId(), WIDTH));
        }
        if (dto.getCategories() != null) {
            post.setCategories(dto.getCategories()
                    .stream()
                    .map(cat -> categoriesService.getCategoryById(cat))
                    .collect(Collectors.toSet()));
        }
        return post;
    }

    public PostDto toPostDto(Post post) {
        PostDto dto = new PostDto();
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setVisibilityId(post.getVisibility().getId());
        if (post.getMedia() != null) {
            dto.setFileUrl(post.getMedia().getPicture());
            dto.setMediaType(post.getMedia().getMediaType());
        }
        Set<Integer> categories = post.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        dto.setCategories(categories);

        return dto;
    }

    public Post mergePost(Post oldPost, PostDto dto) {
        oldPost.setTitle(getNotNull(oldPost.getTitle(), dto.getTitle()));
        oldPost.setDescription(getNotNull(oldPost.getDescription(), dto.getDescription()));
        oldPost.setTimestamp(new Timestamp(System.currentTimeMillis()));
        oldPost.setVisibility(getNotNull(oldPost.getVisibility(), visibilitiesService.getVisibilityById(dto.getVisibilityId())));
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            oldPost.setMedia(mediaMapper.toMedia(dto.getFile(), dto.getVisibilityId(), WIDTH));
        }
        if (dto.getCategories() != null) {
            oldPost.setCategories(dto.getCategories()
                    .stream()
                    .map(cat -> categoriesService.getCategoryById(cat))
                    .collect(Collectors.toSet()));
        }
        return oldPost;
    }
}
