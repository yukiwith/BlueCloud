package com.example.blogservice.controller;

import com.example.blogservice.model.Blog;
import com.example.bolgwithcontents.model.BlogUpload;
import com.example.blogservice.model.Picture;
import com.example.blogservice.repository.BlogRepository;
import com.example.blogservice.repository.PictureRepository;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private StuffServiceImpl stuffService;

    @Autowired
    private RedissonClient redissonClient;


    // 图片存储在服务器的路径
    @Value("${blog.upload-dir}")
    private String uploadDir;

    // 图片存储在数据库里的地址 前端访问的路径（静态的）
    @Value("${blog-fetch-dir}")
    private String dbDir;


    public Page<Blog> getBlogs(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        String key = "page_" + page + "_" + size;
        RMapCache<String, Page<Blog>> cache = redissonClient.getMapCache("home");
        Page<Blog> cachedBlogs = cache.get(key);

        if (cachedBlogs != null) {
            return cachedBlogs;
        }

        Page<Blog> blogs = blogRepository.findAll(pageable);

        // 获取分页的blogId
        List<String> blogIds = blogs.stream().map(Blog::getBlogId).toList();
        // 一次性把所有该页的图片全部查询出来
        List<Picture> pictures = pictureRepository.findByBlogIdIn(blogIds);
        // 图片根据blogId分组
        Map<String, List<String>> picturesMap = pictures.stream()
                .collect(Collectors.groupingBy(Picture::getBlogId, Collectors.mapping(Picture::getPictureUrl, Collectors.toList())));
        // 把图片一个个设置进blog里
        blogs.getContent().forEach(blog -> {
            blog.setPicture(picturesMap.get(blog.getBlogId()));
            // 把商品塞进去
            blog.setStuffList(stuffService.getStuffByBlogId(blog.getBlogId()));
        });

        cache.put(key, blogs, 10, TimeUnit.HOURS);

        return blogs;
    }


    public Optional<Blog> findById(String id) {
        return blogRepository.findBlogByBlogId(id);
    }


    public List<Blog> findByTitle(String title) {
        return blogRepository.findBlogByTitleContaining(title);
    }


    /**
     * 使用事务回滚，存储中（尤其是图片）出现任何问题都会回滚，保证安全
     * @param blogDTO
     */
    // 用GridFsTemplate存储 传输不方便故弃用
//    @Override
//    public void addBlog(BlogDTO blogDTO) {
//        Blog blog = new Blog();
//        blog.setTitle(blogDTO.getTitle());
//        blog.setContent(blogDTO.getContent());
//        blog.setUserId(blogDTO.getUserId());
//        List<String> picture = blogDTO.getPicture().stream().map(file -> {
//            try (InputStream inputStream = file.getInputStream()) {
//                ObjectId objectId = grid.store(inputStream, file.getOriginalFilename(), file.getContentType());
//                return objectId.toHexString();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).toList();
//        blog.setPicture(picture);
//        blogRepository.save(blog);
//    }

    // 使用Base64的方法存储图片数据便于传输


// 这里可以优化：BlogUpload类可以不要。

    @Transactional
    public void addBlog(BlogUpload blogUpload) {
        Blog blog = new Blog();
        blog.setTitle(blogUpload.getTitle());
        blog.setContent(blogUpload.getContent());
        blog.setUserId(blogUpload.getUserId());
        blog.setUserName(blogUpload.getUserName());
        blogRepository.save(blog);

        for (MultipartFile file : blogUpload.getPicture()) {
            try {
                String fileOriginame = file.getOriginalFilename();
                String fileType = fileOriginame.substring(fileOriginame.lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + fileType;
                File dest = new File(uploadDir + fileName);
                file.transferTo(dest);
                Picture picture = new Picture();
                picture.setPictureUrl(dbDir + fileName);
                picture.setBlogId(blog.getBlogId());
                pictureRepository.save(picture);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 删除博客
     * 与其关联的评论区会被clearUtil类定时自动清理掉
     * 图片和商品会被级联删除
     * @param id
     */

    public void deleteBlogById(String id) {
        blogRepository.deleteById(id);
    }



}
