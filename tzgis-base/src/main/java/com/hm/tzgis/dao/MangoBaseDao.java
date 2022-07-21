package com.hm.tzgis.dao;

import com.alibaba.fastjson.JSONArray;
import com.hm.tzgis.entity.MongoMapEntity;
import com.hm.tzgis.entity.MongoObjectEntity;
import com.hm.tzgis.util.MongDBUtils;
import com.hm.tzgis.util.StringUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * mango基本方法封装
 * insert 和 save区别
 * 插入重复数据
 * 　insert: 若新增数据的主键已经存在，则会抛 org.springframework.dao.DuplicateKeyException 异常提示主键重复，不保存当前数据。
 * 　save: 若新增数据的主键已经存在，则会对当前已经存在的数据进行修改操作。
 * 批操作
 * 　	insert: 可以一次性插入一整个列表，而不用进行遍历操作，效率相对较高
 * 　save: 需要遍历列表，进行一个个的插入
 * <p>
 * 最常见的分页采用的是skip+limit这种组合方式，这种方式对付小数据倒也可以，
 * 但是对付上几百上千万的大数据，只能力不从心，skip如果跳过大量的数据会很慢，针对这一情况，
 * 可以通过条件查询+排序+限制返回记录，即 边查询，边排序，排序之后，抽取上一页中的最后一条记录，作为当前分页的查询条件
 * <p>
 * update方法需要自己写
 * 多条件没有
 */
@Service
public class MangoBaseDao<T> {


    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * save对象
     *
     * @param t 实体类
     */
    public void save(T t) {
        mongoTemplate.save(t);
    }

    /**
     * save对象到指定集合
     *
     * @param t              实体类
     * @param collectionName 集合名
     */
    public void save(T t, String collectionName) {
        mongoTemplate.save(t, collectionName);
    }

    /**
     * insert对象
     *
     * @param t 实体类
     */
    public void insert(T t) {
        mongoTemplate.insert(t);
    }

    /**
     * insert对象到指定集合
     *
     * @param t              实体类
     * @param collectionName 集合名
     */
    public void insert(T t, String collectionName) {
        mongoTemplate.insert(t, collectionName);
    }

    /**
     * 批量插入
     *
     * @param t 实体类
     */
    public void insertAll(List<T> t) {
        mongoTemplate.insertAll(t);
    }


    /**
     * 批量插入到指定集合
     *
     * @param t              实体类
     * @param collectionName 集合名
     */
    public void insertAll(List<T> t, String collectionName) {
        mongoTemplate.insert(t, collectionName);
    }


    /**
     * @param collectionName 集合名
     */
    public void dropCollection(String collectionName) {
        try {
            //删除表
            mongoTemplate.dropCollection(collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 指定集合建立索引
     *
     * @param collectionName 集合名
     * @param type           mongoDB索引类型（ASC，DESC，2D，2DSphere，Hashed，Text）
     * @param fieldNames     属性名
     * @return
     */
    public void createIndex(String collectionName, String type, String... fieldNames) {
        List<Bson> bsonList = getIndexes(type, fieldNames);
        bsonList.stream().forEach(s -> {
            mongoTemplate.getCollection(collectionName).createIndex(s);
        });
    }

    public String getAllGeo(String wordid) {
        Query query = new Query().addCriteria(Criteria.where("worldid").is(wordid));
        Query query2 = new Query().addCriteria(Criteria.where("properties.worldid").is(wordid));
        MongoMapEntity mongoMapEntity = mongoTemplate.findOne(query, MongoMapEntity.class, MongDBUtils.MONGODB_ZTT_WORLDID_OBJTYPE);
        List<MongoObjectEntity> mongoObjectEntityList = new ArrayList<>();
        if(mongoMapEntity != null && !StringUtils.isEmpty(mongoMapEntity.getObjTypes())){
            for(String key : mongoMapEntity.getObjTypes().split(",")) {
                List<MongoObjectEntity> mongoObjectEntityTypeList = mongoTemplate.find(query2, MongoObjectEntity.class,MongDBUtils.MONGODB_ZTT + key);
                for(MongoObjectEntity entity : mongoObjectEntityTypeList) {
                    mongoObjectEntityList.add(entity);
                }
            }
        }
        //转换json格式
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("worldid", mongoMapEntity.getWorldid());
        map.put("universe", mongoMapEntity.getUniverse());
//        map.put("worldname",txMapVersion.getMapName());
//        map.put("baseobjtype",txMapVersion.getObjectType());
//        map.put("baseobjid",txMapVersion.getObjectId());

        map.put("viewbox", mongoMapEntity.getViewbox());
        map.put("obj_num", mongoMapEntity.getObjNum());
        map.put("relationlines", mongoMapEntity.getRelationlines());
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(MongoObjectEntity mongoObjectEntity: mongoObjectEntityList) {
            Map<String,Object> mongoObjectMap = new LinkedHashMap<>();
            mongoObjectMap.put("type", mongoObjectEntity.getType());
            mongoObjectMap.put("properties", mongoObjectEntity.getProperties());
            Map<String,Object> geomeryMap = new HashMap<>();
            if(mongoObjectEntity.getGeometry() != null) {
                geomeryMap.put("type", mongoObjectEntity.getGeometry().get("type"));
                geomeryMap.put("coordinates", JSONArray.parseArray(mongoObjectEntity.getGeometry().get("coordinates"), Object.class));
                mongoObjectMap.put("geometry", geomeryMap);
            }else {
                //System.out.println(mongoObjectEntity.getProperties());
            }
            mapList.add(mongoObjectMap);
        }
        map.put("zttobjs", mapList);

        return JSONArray.toJSONString(map);
    }



    public void createUniqueIndex(String collectionName, String... fieldNames) {
        for (String fieldName : fieldNames) {
            mongoTemplate.getCollection(collectionName).createIndex(new Document(fieldName, 1),
                    new IndexOptions().unique(true));
        }
    }

    /**
     * 获取所有类型对象
     *
     * @param t
     * @return
     */
    public List<T> findAll(Class<T> t) {
        return mongoTemplate.findAll(t);
    }

    /**
     * 获取指定集合所有类型对象
     *
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public List<T> findAll(Class<T> t, String collectionName) {
        return mongoTemplate.findAll(t, collectionName);
    }

    /**
     * 查询排序集合
     *
     * @param t
     * @param collectionName
     * @param sort
     * @return
     */
    public List<T> findAllSort(Class<T> t, String collectionName, String sort) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, sort));
        return mongoTemplate.find(query, t, collectionName);
    }


    /**
     * 去重查询字段数据
     *
     * @param t
     * @param collectionName
     * @param field
     * @param query
     * @return
     */
    public List<T> findDistinct(Class<T> t, String collectionName, String field, Query query) {
        return mongoTemplate.findDistinct(query, field, collectionName, t);
    }

    /**
     * 根据id获取对象
     *
     * @param id 主键
     * @param t  实体类
     * @return
     */
    public T findById(String id, Class<T> t) {
        Query query = new Query(Criteria.where("id").is(id));
        T m = mongoTemplate.findOne(query, t);
        return m;
    }

    /**
     * 指定集合根据id获取对象
     *
     * @param id             主键
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public T findById(String id, Class<T> t, String collectionName) {
        Query query = new Query(Criteria.where("id").is(id));
        T m = mongoTemplate.findOne(query, t, collectionName);
        return m;
    }

    /**
     * 指定集合根据id获取对象
     *
     * @param value           值
     * @param column          列
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public T findByColumn(String value,String column, Class<T> t, String collectionName) {
        Query query = new Query(Criteria.where(column).is(value));
        T m = mongoTemplate.findOne(query, t, collectionName);
        return m;
    }

    /**
     * 指定集合根据id获取对象
     *
     * @param str            查询字段
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public List<T> findByRegex(String whereName, String str, Class<T> t, String collectionName) {
        Query query = new Query(Criteria.where(whereName).regex("^" + str, "m"));
        List<T> list = mongoTemplate.find(query, t, collectionName);
        return list;
    }

    /**
     * 指定集合根据id获取对象
     *
     * @param str            查询字段
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public List<T> findByAllRegex(String whereName, String str, Class<T> t, String collectionName) {
        Query query = new Query(Criteria.where(whereName).regex(str, "m").and("type").ne(null));
        List<T> list = mongoTemplate.find(query, t, collectionName);
        return list;
    }

    public List<T> findByRegexZtname(String whereName, String str, Class<T> t, String collectionName) {
        Query query = new Query(Criteria.where(whereName).regex(str, "m"));
        List<T> list = mongoTemplate.find(query, t, collectionName);
        return list;
    }

    /**
     * 指定集合根据id获取对象
     *
     * @param whereName      mogonDB字段
     * @param list           查询数组
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public List<T> findByRegex(String whereName, List<String> list, Class<T> t, String collectionName) {
        Criteria[] arr = new Criteria[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Criteria.where(whereName).regex("^" + list.get(i), "m");
        }
        Query query = new Query().addCriteria(new Criteria().orOperator(arr));
        List<T> list2 = mongoTemplate.find(query, t, collectionName);
        return list2;
    }


    /**
     * 指定集合根据id获取对象
     *
     * @param list           查询数组
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public List<T> findByIds(List<String> list, String whereName, Class<T> t, String collectionName) {
        Criteria[] arr = new Criteria[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Criteria.where(whereName).is(list.get(i));
        }
        Query query = new Query().addCriteria(new Criteria().orOperator(arr));
        List<T> list2 = mongoTemplate.find(query, t, collectionName);
        return list2;
    }

    /**
     * 指定集合根据id获取对象
     *
     * @param whereName      mogonDB字段
     * @param list           查询数组
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public List<T> findByLineRegex(String whereName, List<String> list, Class<T> t, String collectionName) {
        Criteria[] arr = new Criteria[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Criteria.where(whereName).regex(list.get(i));
        }
        Query query = new Query().addCriteria(new Criteria().orOperator(arr));
        List<T> list2 = mongoTemplate.find(query, t, collectionName);
        return list2;
    }

    /**
     * 指定集合根据id获取对象
     *
     * @param whereName      mogonDB字段
     * @param list           查询数组
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public List<T> findAllByName(String whereName, List<String> list, Class<T> t, String collectionName) {
        Query query = new Query().addCriteria(Criteria.where(whereName).in(list));
        List<T> list2 = mongoTemplate.find(query, t, collectionName);
        return list2;
    }

    /**
     * 根据具体条件获取对象（单条数据）
     *
     * @param whereName mongoDB属性
     * @param name      查询字段
     * @param t         实体类
     * @return
     */
    public T findByName(String whereName, String name, Class<T> t) {
        Query query = new Query(Criteria.where(whereName).is(name));
        T m = mongoTemplate.findOne(query, t);
        return m;
    }

    /**
     * 指定集合根据具体条件获取对象（单条数据）
     *
     * @param whereName      mongoDB属性
     * @param name           查询字段
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public T findByName(String whereName, String name, Class<T> t, String collectionName) {
        Query query = new Query(Criteria.where(whereName).is(name));
        T m = mongoTemplate.findOne(query, t, collectionName);
        return m;
    }

    /**
     * 根据具体条件获取对象（所有数据）
     *
     * @param whereName mongoDB属性
     * @param name      查询字段
     * @param t         实体类
     * @return
     */
    public List<T> findAllByName(String whereName, String name, Class<T> t) {
        Query query = new Query(Criteria.where(whereName).is(name));
        List<T> list = mongoTemplate.find(query, t);
        return list;
    }

    /**
     * 根据具体条件获取对象（所有数据）
     *
     * @param whereName      mongoDB属性
     * @param name           查询字段
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public List<T> findAllByName(String whereName, Object name, Class<T> t, String collectionName) {
        Query query = new Query(Criteria.where(whereName).is(name));
        List<T> list = mongoTemplate.find(query, t, collectionName);
        return list;
    }

    /**
     * 查询排序
     *
     * @param whereName 字段名
     * @param where     条件
     * @param name      值
     * @param sort      排序：asc或ASC升序   desc或DESC降序
     * @return
     */
    public List<T> findByNameSort(String whereName, String where, Object name, String sort, Class<T> t) {
        Query query = new Query(getWhere(whereName, where, name));
        if (sort != null) {
            query.with(getSort(whereName, sort));
        }
        List<T> list = mongoTemplate.find(query, t);
        return list;
    }

    /**
     * 指定集合查询排序
     *
     * @param whereName      字段名
     * @param where          条件
     * @param name           值
     * @param sort           排序：asc或ASC升序   desc或DESC降序
     * @param collectionName 集合名
     * @return
     */
    public List<T> findByNameSort(String whereName, String where, Object name, String sort, Class<T> t, String collectionName) {
        Query query = new Query(getWhere(whereName, where, name));
        if (sort != null) {
            query.with(getSort(whereName, sort));
        }
        List<T> list = mongoTemplate.find(query, t, collectionName);
        return list;
    }

    /**
     * 分页查询
     *
     * @param whereName 字段名
     * @param where     条件
     * @param name      值
     * @param pageSize  页码
     * @param pageNum   条数
     * @param sort      排序：asc或ASC升序   desc或DESC降序
     * @return
     */
    public List<T> findByNamePagination(String whereName, String where, Object name, int pageSize, int pageNum, String sort, Class<T> t) {
        Query query = new Query(getWhere(whereName, where, name));
        query.skip((pageSize - 1) * pageNum).limit(pageNum);
        if (sort != null) {
            query.with(getSort(whereName, sort));
        }
        List<T> list = mongoTemplate.find(query, t);
        return list;
    }


    /**
     * 指定集合分页查询
     *
     * @param whereName      字段名
     * @param where          条件
     * @param name           值
     * @param pageSize       页码
     * @param pageNum        条数
     * @param sort           排序：asc或ASC升序   desc或DESC降序
     * @param collectionName 集合名
     * @return
     */
    public List<T> findByNamePagination(String whereName, String where, Object name, int pageSize, int pageNum, String sort, Class<T> t, String collectionName) {
        Query query = new Query(getWhere(whereName, where, name));
        query.skip((pageSize - 1) * pageNum).limit(pageNum);
        if (sort != null) {
            query.with(getSort(whereName, sort));
        }
        List<T> list = mongoTemplate.find(query, t, collectionName);
        return list;
    }

    /**
     * 根据具体条件获取对象（所有数据）
     *
     * @param whereName      mongoDB属性
     * @param name           查询字段
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public List<T> findDeviceByName(String whereName, String name, Class<T> t, String collectionName) {
        Query query = new Query(Criteria.where(whereName).is(name));
        query.fields().include("id").include("geometry").include("properties");
        List<T> list = mongoTemplate.find(query, t, collectionName);
        return list;
    }

    /**
     * 根据具体条件获取对象（单个具体数据）
     *
     * @param whereName      mongoDB属性
     * @param name           查询字段
     * @param c              实体类
     * @param collectionName 集合名
     * @return
     */
    public T findDeviceByDeviceId(String whereName, String name, Class<T> c, String collectionName) {
        Query query = new Query(Criteria.where(whereName).is(name));
        query.fields().include("id").include("geometry").include("properties");
        T t = mongoTemplate.findOne(query, c, collectionName);
        return t;
    }

    /**
     * 根据具体条件获取对象（所有数据）
     *
     * @param whereName      mongoDB属性
     * @param name           查询字段
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public List<T> findTreeDeviceByName(String whereName, String name, Class<T> t, String collectionName) {
        Query query = new Query(Criteria.where(whereName).is(name));
        query.fields().include("id").include("properties").include("geometry");
        List<T> list = mongoTemplate.find(query, t, collectionName);
        return list;
    }

    /**
     * 根据具体条件获取对象（所有数据）
     *
     * @param whereName      mongoDB属性
     * @param name           查询字段
     * @param c              实体类
     * @param collectionName 集合名
     * @return
     */
    public T findDeviceName(String whereName, String name, Class<T> c, String collectionName) {
        Query query = new Query(Criteria.where(whereName).is(name));
        query.fields().include("modelAlias").include("show");
        T t = mongoTemplate.findOne(query, c, collectionName);
        return t;
    }

    /**
     * 自定义条件
     *
     * @param criteria = Criteria.where("name").is(name).and("size").is(size).and("age").is(age)
     * @param pageable 排序参数 = Sort.by(Sort.Order.asc("readOrNot"),Sort.Order.asc("readOrNot"))
     * @return
     */
    public List<T> findByCriteria(Criteria criteria, Pageable pageable, Class<T> t) {
        Query query = new Query(criteria);
        if (pageable != null) {
            query.with(pageable);
        }
        List<T> list = mongoTemplate.find(query, t);
        return list;
    }

    /**
     * 指定集合自定义条件
     *
     * @param criteria       = Criteria.where("name").is(name).and("size").is(size).and("age").is(age)
     * @param pageable       排序参数 = Sort.by(Sort.Order.asc("readOrNot"),Sort.Order.asc("readOrNot"))
     * @param collectionName 集合名
     * @return
     */
    public List<T> findByCriteria(Criteria criteria, Pageable pageable, Class<T> t, String collectionName) {
        Query query = new Query(criteria);
        if (pageable != null) {
            query.with(pageable);
        }
        List<T> list = mongoTemplate.find(query, t, collectionName);
        return list;
    }

    /**
     * 自定义分页条件
     *
     * @param criteria = Criteria.where("name").is(name).and("size").is(size).and("age").is(age)
     * @param pageSize 页码
     * @param pageNum  条数
     * @param pageable 排序参数 = Sort.by(Sort.Order.asc("readOrNot"),Sort.Order.asc("readOrNot"))
     * @return
     */
    public List<T> findByCriteria(Criteria criteria, int pageSize, int pageNum, Pageable pageable, Class<T> t) {
        Query query = new Query(criteria);
        query.skip((pageSize - 1) * pageNum).limit(pageNum);
        if (pageable != null) {
            query.with(pageable);
        }
        List<T> list = mongoTemplate.find(query, t);
        return list;
    }

    /**
     * 自定义分页条件
     *
     * @param criteria       = Criteria.where("name").is(name).and("size").is(size).and("age").is(age)
     * @param pageSize       页码
     * @param pageNum        条数
     * @param pageable       排序参数 = Sort.by(Sort.Order.asc("readOrNot"),Sort.Order.asc("readOrNot"))
     * @param collectionName 集合名
     * @return
     */
    public List<T> findByCriteria(Criteria criteria, int pageSize, int pageNum, Pageable pageable, Class<T> t, String collectionName) {
        Query query = new Query(criteria);
        query.skip((pageSize - 1) * pageNum).limit(pageNum);
        if (pageable != null) {
            query.with(pageable);
        }
        List<T> list = mongoTemplate.find(query, t, collectionName);
        return list;
    }

    /**
     * 更新对象
     * 演示 还需自己写
     */
    public void updateTest(T t) {
        //	        Query query=new Query(Criteria.where("id").is(test.getId()));
        //	        Update update= new Update().set("age", test.getAge()).set("name", test.getName());
        //更新查询返回结果集的第一条
        //	        mongoTemplate.updateFirst(query,update,getTClass());
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,TestEntity.class);
    }

    /**
     * 更新查询返回结果集的第一条
     *
     * @param query
     * @param update
     */
    public void updateFirst(Query query, Update update, Class<T> t) {
        //	        Query query=new Query(Criteria.where("id").is(test.getId()));
        //	        Update update= new Update().set("age", test.getAge()).set("name", test.getName());
        mongoTemplate.updateFirst(query, update, t);

    }

    /**
     * 更新查询返回结果集的所有
     *
     * @param query
     * @param update
     */
    public void updateMulti(Query query, Update update, Class<T> t) {
        //	        Query query=new Query(Criteria.where("id").is(test.getId()));
        //	        Update update= new Update().set("age", test.getAge()).set("name", test.getName());
        mongoTemplate.updateMulti(query, update, t);
    }

    /**
     * 根据id删除对象
     *
     * @param id 主键值
     * @param t  实体类
     */
    public void deleteById(Integer id, Class<T> t) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, t);
    }

    /**
     * 根据id删除对象
     *
     * @param id 主键值
     * @param t  实体类
     */
    public void deleteById(String id, Class<T> t) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, t);
    }

    /**
     * 指定集合根据id删除对象
     *
     * @param id             主键值
     * @param t              实体类
     * @param collectionName 集合名
     */
    public void deleteById(String id, Class<T> t, String collectionName) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, t, collectionName);
    }

    public long deleteByWhere(Query query, Class<T> t, String collectionName) {
        DeleteResult result = mongoTemplate.remove(query, t, collectionName);
        return result.getDeletedCount();
    }

    /**
     * 指定集合根据id删除对象
     *
     * @param list           查询数组
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public void deleteByIds(List<String> list, String whereName, Class<T> t, String collectionName) {
//        Criteria[] arr = new Criteria[list.size()];
//        for (int i = 0; i < arr.length; i++) {
//            arr[i] = Criteria.where(whereName).is(list.get(i));
//        }
//        Query query = new Query().addCriteria(new Criteria().orOperator(arr));
//       mongoTemplate.remove(query, t, collectionName);

        String[] arr = list.toArray(new String[list.size()]);
        Criteria criteria = Criteria.where(whereName).in(arr);
        Query query = new Query(criteria);
        mongoTemplate.remove(query, t, collectionName);

    }

    /**
     * 根据条件名称删除
     *
     * @param whereName 条件名称
     * @param name      条件值
     * @param t         实体类
     */
    public void deleteByName(String whereName, String name, Class<T> t) {
        Query query = new Query(Criteria.where(whereName).is(name));
        mongoTemplate.remove(query, t);
    }

    /**
     * 指定集合根据条件名称删除
     *
     * @param whereName      条件名称
     * @param name           条件值
     * @param t              实体类
     * @param collectionName 集合名
     */
    public DeleteResult deleteByName(String whereName, String name, Class<T> t, String collectionName) {
        Query query = new Query(Criteria.where(whereName).is(name));
        return mongoTemplate.remove(query, t, collectionName);
    }

    /**
     * 根据条件名称删除
     *
     * @param whereName 条件名称
     * @param name      条件值
     * @param t         删除对象
     */
    public void deleteByWhereName(String whereName, String where, Object name, Class<T> t) {
        Query query = new Query(getWhere(whereName, where, name));
        mongoTemplate.remove(query, t);
    }

    /**
     * 指定集合根据条件名称删除
     *
     * @param whereName      条件名称
     * @param name           条件值
     * @param t              删除对象
     * @param collectionName 集合名
     */
    public void deleteByWhereName(String whereName, String where, Object name, Class<T> t, String collectionName) {
        Query query = new Query(getWhere(whereName, where, name));
        mongoTemplate.remove(query, t, collectionName);
    }

    /**
     * 删除对象
     *
     * @param t
     */
    public void delete(T t) {
        mongoTemplate.remove(t);
    }

    /**
     * 删除所有类型对象
     */
    public void deleteAll(Class<T> t) {
        mongoTemplate.remove(t);
    }

    /**
     * 查用查询规则
     *
     * @param whereName 字段名称
     * @param where     条件
     * @param name      值
     */
    public Criteria getWhere(String whereName, String where, Object name) {
        Criteria c = Criteria.where(whereName);
        if (where.equals("gt")) {            //	大于
            c = c.gt(name);
        } else if (where.equals("gte")) {  //	大于等于
            c = c.gte(name);
        } else if (where.equals("in")) {   //	包含  数组对象
            c = c.in(name);
        } else if (where.equals("is")) {   //	等于
            c = c.is(name);
        } else if (where.equals("lt")) {   //	小于
            c = c.lt(name);
        } else if (where.equals("lte")) {   //	小等于
            c = c.lte(name);
        } else if (where.equals("nin")) {   //	不包含 数组对象
            c = c.nin(name);
        } else if (where.equals("regex")) {   // 模糊查询  Pattern对象改编  .* +name +.* 表示左匹配或者右匹配
            c = c.regex((String) name);
        }
        return c;
    }

    /**
     * 索引规则
     *
     * @param type   mongoDB索引类型（ASC，DESC，2D，2DSphere，Hashed，Text）
     * @param fields 需要添加索引的属性
     * @return
     */
    public List<Bson> getIndexes(String type, String... fields) {
        List<Bson> bsonlist = new ArrayList<>();
        if ("ASC".equals(type)) {
            for (String s : fields) {
                bsonlist.add(Indexes.ascending(s));
            }
        } else if ("DESC".equals(type)) {
            for (String s : fields) {
                bsonlist.add(Indexes.descending(s));
            }
        } else if ("2D".equals(type)) {
            for (String s : fields) {
                bsonlist.add(Indexes.geo2d(s));
            }
        } else if ("2DSphere".equals(type)) {
            for (String s : fields) {
                bsonlist.add(Indexes.geo2dsphere(s));
            }
        } else if ("Hashed".equals(type)) {
            for (String s : fields) {
                bsonlist.add(Indexes.hashed(s));
            }
        } else if ("Text".equals(type)) {
            for (String s : fields) {
                bsonlist.add(Indexes.text(s));
            }
        }
        return bsonlist;
    }

    /**
     * 排序规则
     *
     * @param whereName 排序字段
     * @param sort      排序：asc或ASC升序   desc或DESC降序
     */
    public Sort getSort(String whereName, String sort) {
        Sort s = null;
        if ("asc".equals(sort) || "ASC".equals(sort)) {//升序
            s = Sort.by(Sort.Direction.ASC, whereName);
        } else if ("desc".equals(sort) || "DESC".equals(sort)) {//降序
            s = Sort.by(Sort.Direction.DESC, whereName);
        }
        return s;
    }

    /*************************************************分界线 - 保留原有方法********************************************************/
    /**
     * 新增对象类型
     *
     * @param t
     */
    public void insert(Class<T> t) {
        mongoTemplate.insert(t);
    }

    /**
     * 获取所有类型对象
     *
     * @return
     */
    public List<T> findEntityAll(Class<T> t) {
        return mongoTemplate.findAll(t);
    }

    /**
     * 指定集合中获取对象集合
     *
     * @param collectionName
     * @return
     */
    public List<T> findByCollectionName(String collectionName, Class<T> t) {
        return mongoTemplate.findAll(t, collectionName);
    }

    public List<T> findByNameList(String whereName, String name, Class<T> t) {
        Query query = new Query(Criteria.where(whereName).is(name));
        List<T> list = mongoTemplate.find(query, t);
        return list;
    }

    /**
     * 查询排序
     *
     * @param whereName 字段名
     * @param where     条件
     * @param name      值
     * @param pageable  排序参数 = Sort.by(Sort.Order.asc("readOrNot"),Sort.Order.asc("readOrNot"))
     * @return
     */
    public List<T> findByWhereName(String whereName, String where, Object name, Pageable pageable, Class<T> t) {
        Query query = new Query(getWhere(whereName, where, name));
        if (pageable != null) {
            query.with(pageable);
        }
        List<T> list = mongoTemplate.find(query, t);
        return list;
    }

    /**
     * 分页查询
     *
     * @param whereName 字段名
     * @param where     条件
     * @param name      值
     * @param pageSize  页码
     * @param pageNum   条数
     * @param pageable  排序参数 = Sort.by(Sort.Order.asc("readOrNot"),Sort.Order.asc("readOrNot"))
     * @return
     */
    public List<T> findByWhereName(String whereName, String where, Object name, int pageSize, int pageNum, Pageable pageable, Class<T> t) {
        Query query = new Query(getWhere(whereName, where, name));
        query.skip((pageSize - 1) * pageNum).limit(pageNum);
        if (pageable != null) {
            query.with(pageable);
        }
        List<T> list = mongoTemplate.find(query, t);
        return list;
    }

    /**
     * 批量插入到指定集合并建立索引
     *
     * @param t                实体类
     * @param collectionName   集合名
     * @param type             mongoDB索引类型（ASC，DESC，2D，2DSphere，Hashed，Text）
     * @param dSphereType      空间索引类型
     * @param dSphereTypeField 空间索引类型属性名
     * @param type             普通索引类型
     * @param typeField        普通索引属性名
     */
    public void insertAllAndIndex(List<T> t, String collectionName, String dSphereType, String dSphereTypeField, String type, String... typeField) {

        //分片集群使用
//        StringBuffer sb = new StringBuffer();
//        sb.append("{shardcollection : 'GeoServer.").append(collectionName).append("',key:{_id:'hashed'}}");
//        secondaryMongoTemplate.executeCommand(sb.toString());
        //分片集群使用


        mongoTemplate.insert(t, collectionName);
        MongoCollection<Document> mongoCollecion = mongoTemplate.getCollection(collectionName);
        getIndexes(dSphereType, dSphereTypeField).forEach(s -> {
            mongoCollecion.createIndex(s);
        });
        getIndexes(type, typeField).forEach(s -> {
            mongoCollecion.createIndex(s);
        });
    }

    /**
     * 查询圆圈范围内的数据
     *
     * @param centerX
     * @param centerY
     * @param radius
     * @param whereName
     * @param t
     * @param collectionName
     * @return
     */
    public List<T> queryWithinCircle(double centerX, double centerY, double radius, String whereName, Class t, String collectionName) {
        return mongoTemplate.find(new Query(Criteria.where(whereName).within(new Circle(centerX, centerY, radius))), t, collectionName);
    }

    /***
     * @param query
     * @param entityClass
     * @param collectionName
     * @return
     */
    public List<T> find(Query query, Class<T> entityClass, String collectionName) {
        return mongoTemplate.find(query, entityClass, collectionName);
    }


    /**
     * 获取所有集合名
     *
     * @return
     */
    public List<String> findAllTables() {
        Set<String> tableSet = mongoTemplate.getCollectionNames();
        List<String> tableList = new ArrayList<>();
        tableSet.stream().sorted().forEach(t -> {
            if (t.matches("[0-9]+")) {
                tableList.add(t);
            }
        });
        return tableList;
    }

    /**
     * 指定集合根据id获取对象
     *
     * @param whereName      mogonDB字段
     * @param list           查询数组
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public List<T> findAllCompany(String whereName, List<String> list, Class<T> t, String collectionName) {
        Query query = new Query().addCriteria(Criteria.where(whereName).in(list));
        List<T> list2 = mongoTemplate.find(query, t, collectionName);
        return list2;
    }


    public void updateMulti(Query query, Update update, String collectionName) {
        mongoTemplate.updateMulti(query, update, collectionName);
    }

    /**
     * 指定集合根据运行单位获取对象
     *
     * @param yxdw           运行单位
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public List<T> findByYxdw(String yxdw, Class<T> t, String collectionName) {
        Query query = new Query().addCriteria(Criteria.where("yxdw").is(yxdw));
        List<T> list = mongoTemplate.find(query, t, collectionName);
        return list;
    }


    public List<T> findAllByNameFordydj(String whereName, List<String> list, Class<T> t, String collectionName, List<String> dyList, String ssds, String yxdw) {
        Query query = new Query().addCriteria(Criteria.where(whereName).in(list));
        if (dyList != null && dyList.size() > 0)
            query.addCriteria(Criteria.where("properties.dydj").in(dyList));
        if (ssds != null)
            query.addCriteria(Criteria.where("properties.ssds").is(ssds));
        if (yxdw != null)
            query.addCriteria(Criteria.where("properties.yxdw").is(yxdw));
        List<T> list2 = mongoTemplate.find(query, t, collectionName);
        return list2;
    }


    /**
     * 指定集合根据id获取对象
     *
     * @param whereName      mogonDB字段
     * @param list           查询数组
     * @param t              实体类
     * @param collectionName 集合名
     * @return
     */
    public List<T> findByLineRegexdydj(String whereName, List<String> list, Class<T> t, String collectionName, List<String> dyList, String ssds, String yxdw) {

        Criteria[] arr = new Criteria[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Criteria.where(whereName).regex(list.get(i));
        }

        Query query = new Query().addCriteria(new Criteria().orOperator(arr));
        if (dyList != null && dyList.size() > 0)
            query.addCriteria(Criteria.where("properties.dydj").in(dyList));
        if (ssds != null)
            query.addCriteria(Criteria.where("properties.ssds").is(ssds));
        if (yxdw != null)
            query.addCriteria(Criteria.where("properties.yxdw").is(yxdw));
        List<T> list2 = mongoTemplate.find(query, t, collectionName);
        return list2;
    }
    public List<T> findByLineForPoind(String whereName, List<String> list, Class<T> t, String collectionName, List<String> dyList,String ssds,String yxdw) {

        Criteria[] arr = new Criteria[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Criteria.where(whereName).regex(list.get(i),null);
        }

        Query query = new Query().addCriteria(new Criteria().orOperator(arr));
       
        if (dyList != null && dyList.size() > 0)
            query.addCriteria(Criteria.where("properties.dydj").in(dyList));
        if (ssds != null ) 
            query.addCriteria(Criteria.where("properties.ssds").is(ssds));
        if (yxdw != null ) 
            query.addCriteria(Criteria.where("properties.yxdw").is(yxdw));
        if (collectionName.equals("710"))
        	query.addCriteria(Criteria.where("properties.sbzlx").is("10000201"));
        if (collectionName.equals("7102")) {
        	query.addCriteria(Criteria.where("properties.sbzlx").is("10000200"));
        	collectionName="710";
        }
        query.addCriteria(Criteria.where("properties.shape").ne(null));
        List<T> list2 = mongoTemplate.find(query, t, collectionName);
        return list2;
    }
    /**
     * @param pageSize
     * @param pageNum
     * @param sortName
     * @param sort
     * @param t
     * @param collectionName
     * @return
     */
    public List<T> findByPage(int pageSize, int pageNum, String sortName, String sort, Class<T> t, String collectionName) {
        Query query = new Query();
        query.skip((pageSize - 1) * pageNum).limit(pageNum);
        query.with(getSort(sortName, sort));
        return mongoTemplate.find(query, t, collectionName);
    }

    /**
     * 查询表总数据量
     *
     * @param collectionName
     * @return
     */
    public long findCount(String collectionName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").ne(null));
        return mongoTemplate.count(query, collectionName);
    }

    /**
     * 分页查询
     *
     * @param pageSize       页面大小
     * @param sortName       排序字段
     * @param sort           排序
     * @param skipVal        跳过的val
     * @param t              t
     * @param collectionName 集合名称
     * @return {@link List}<{@link T}>
     */
    public List<T> findByPage(int pageSize, String sortName, String sort, String skipVal, Class<T> t, String collectionName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("oid").gt(skipVal).and("type").ne(null)).with(getSort(sortName, sort)).limit(pageSize);
        return mongoTemplate.find(query, t, collectionName);
    }

}
