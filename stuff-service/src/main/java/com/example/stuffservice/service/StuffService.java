package com.example.bolgwithcontents.service.impl;

import com.example.bolgwithcontents.model.Stuff;
import com.example.bolgwithcontents.model.StuffPicture;
import com.example.bolgwithcontents.repository.BlogStuffConnectedRepository;
import com.example.bolgwithcontents.repository.StuffPictureRepository;
import com.example.bolgwithcontents.repository.StuffRepository;
import com.example.bolgwithcontents.service.StuffService;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
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
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Service
public class StuffServiceImpl implements StuffService {

    @Autowired
    private StuffRepository stuffRepository;

    @Autowired
    private StuffPictureRepository stuffPictureRepository;

    @Autowired
    private BlogStuffConnectedRepository blogStuffConnectedRepository;

    @Autowired
    private RedissonClient redissonClient;

    @Value("${stuff.upload-dir}")
    private String uploadDir;

    @Value("${stuff-fetch-dir}")
    private String dbDir;


    /**
     * 根据博客返回该博客相关的商品
     * 方便在博客页展示商品
     *
     */
//    public List<Stuff> getStuffByBlogId(String blogId) {
//
//        // 1.根据blogId获取博客包含的所有商品
//        List<Long> stuffIds = blogStuffConnectedRepository.findStuffIdsByBlogId(blogId);
//        // 2.根据这些stuffId获取到所有商品对象
//        List<Stuff> stuffs = stuffRepository.findByStuffIdIn(stuffIds);
//        // 3.根据stuffId获取所有图片
//        List<StuffPicture> stuffPictures = stuffPictureRepository.findByStuffIdIn(stuffIds);
//        // 4.组装stuff返回集合
//        // 4.1 把图片根据stuffId进行分组
//        Map<Long, List<String>> pictureMaps = stuffPictures.stream()
//                .collect(Collectors.groupingBy(StuffPicture::getStuffId,
//                         Collectors.mapping(StuffPicture::getPictureUrl, Collectors.toList())));
//        // 4.2 把分组好的图片挨个插到对应商品里
//        stuffs.forEach(stuff -> {
//            List<String> pictures = pictureMaps.get(stuff.getStuffId());
//            stuff.setStuffPicture(pictures);
//        });
//        return stuffs;
//    }

    @Override
    public List<Stuff> getStuffByBlogId(String blogId) {

        // 1.根据blogId获取博客包含的所有商品
        List<Long> stuffIds = blogStuffConnectedRepository.findStuffIdsByBlogId(blogId);

        // 2.根据这些stuffId获取到所有商品对象
        List<Stuff> stuffs = stuffRepository.findByStuffIdIn(stuffIds);
        // 3.根据stuffId获取所有图片
        List<StuffPicture> stuffPictures = stuffPictureRepository.findByStuffIdIn(stuffIds);
        // 4.组装stuff返回集合
        // 4.1 把图片根据stuffId进行分组
        Map<Long, List<String>> pictureMaps = stuffPictures.stream()
                .collect(Collectors.groupingBy(StuffPicture::getStuffId,
                        Collectors.mapping(StuffPicture::getPictureUrl, Collectors.toList())));
        // 4.2 把分组好的图片挨个插到对应商品里
        stuffs.forEach(stuff -> {
            List<String> pictures = pictureMaps.get(stuff.getStuffId());
            stuff.setStuffPicture(pictures);
        });
        return stuffs;
    }

    /**
     *
     *  商城浏览页的分页查询
     */
    @Override
    public Page<Stuff> getStuffs(int page) {
        Pageable pageable = PageRequest.of(page,20);
        String key = "page" + page + "_" + 20;
        RMapCache<String, Page<Stuff>> cache = redissonClient.getMapCache("stuffPage");
        Page<Stuff> stuffsCache = cache.get(key);

        if (stuffsCache != null) {
            return stuffsCache;
        }

        Page<Stuff> stuffs = stuffRepository.findAll(pageable);
        List<Long> stuffIds = stuffs.stream().map(Stuff::getStuffId).toList();
        // 3.根据stuffId获取所有图片
        List<StuffPicture> stuffPictures = stuffPictureRepository.findByStuffIdIn(stuffIds);
        // 4.组装stuff返回集合
        // 4.1 把图片根据stuffId进行分组
        Map<Long, List<String>> pictureMaps = stuffPictures.stream()
                .collect(Collectors.groupingBy(StuffPicture::getStuffId,
                        Collectors.mapping(StuffPicture::getPictureUrl, Collectors.toList())));
        // 4.2 把分组好的图片挨个插到对应商品里
        stuffs.getContent().forEach(stuff -> {
            List<String> pictures = pictureMaps.get(stuff.getStuffId());
            stuff.setStuffPicture(pictures);
        });

        // 库存预先加载进redis
        for (Stuff stuff : stuffs.getContent()) {
            String stuffKey = "stuff_" + stuff.getStuffId();
            String stockKey = "stock_" + stuff.getStuffId();
            RBucket<Stuff> stuffCache = redissonClient.getBucket(stuffKey);
            stuffCache.set(stuff);
            RAtomicLong stockCache = redissonClient.getAtomicLong(stockKey);
            stockCache.set(stuff.getStock());
        }
        cache.put(key, stuffs, 5, TimeUnit.HOURS);

        return stuffs;
    }






    @Transactional
    @Override
    public void add(Stuff stuff, List<MultipartFile> picture) {
        stuffRepository.save(stuff);
        try {
            for (MultipartFile file : picture) {
                String originFileName = file.getOriginalFilename();
                String fileType = originFileName.substring(originFileName.lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + fileType;
                File dest = new File(uploadDir + fileName);
                file.transferTo(dest);
                StuffPicture stuffPicture = new StuffPicture();
                stuffPicture.setPictureUrl(dbDir + fileName);
                stuffPicture.setStuffId(stuff.getStuffId());
                stuffPictureRepository.save(stuffPicture);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public long addStock(Long stuffId, int amount) {
        stuffRepository.updateStockByStuffId(stuffId, amount);
        RAtomicLong stock = redissonClient.getAtomicLong("stock_" + stuffId);
        return stock.addAndGet(amount);
    }

    public void delete(Long stuffId) {
        stuffRepository.deleteById(stuffId);
    }


}
