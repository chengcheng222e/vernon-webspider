/**
 * 
 */
package com.vernon.webspider.core.image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.vernon.webspider.core.util.JSONUtil;

/**
 * 图片压缩的返回对象
 * 
 * @author Vernon.Chen
 *
 */
@SuppressWarnings("serial")
public class Response
        implements Serializable {

    @Expose
    private Entity srcEntity;
    @Expose
    private List<Entity> destEntities;

    /**
     * @return the srcEntity
     */
    public final Entity getSrcEntity() {
        return this.srcEntity;
    }

    /**
     * @param srcEntity
     *            the srcEntity to set
     */
    public final void setSrcEntity(Entity srcEntity) {
        this.srcEntity = srcEntity;
    }

    /**
     * @return the destEntities
     */
    public final List<Entity> getDestEntities() {
        return this.destEntities;
    }

    /**
     * @param destEntities
     *            the destEntities to set
     */
    public final void setDestEntities(List<Entity> destEntities) {
        this.destEntities = destEntities;
    }

    public static void main(String[] args) {

        Entity srcEntity = new Entity();
        srcEntity.setCode("200");
        srcEntity.setURI("/data/txweb/1.jpg");
        srcEntity.setFormat("160 * 320");
        srcEntity.setHeight(155);
        srcEntity.setWidth(320);
        srcEntity.setSize(1203);

        List<Entity> destEntities = new ArrayList<Entity>();
        Entity destEntity = new Entity();
        destEntity.setCode("200");
        destEntity.setURI("/data/txweb/1_120_280.jpg");
        destEntity.setFormat("120 * 280");
        destEntity.setHeight(68);
        destEntity.setWidth(280);
        destEntity.setSize(740);
        destEntities.add(destEntity);

        destEntity = new Entity();
        destEntity.setCode("200");
        destEntity.setURI("/data/txweb/1_20_20.jpg");
        destEntity.setFormat("20 * 20");
        destEntity.setHeight(20);
        destEntity.setWidth(20);
        destEntity.setSize(120);
        destEntities.add(destEntity);

        Response response = new Response();
       
        response.setSrcEntity(srcEntity);
        response.setDestEntities(destEntities);
        System.out.println(JSONUtil.toJSON(response));
    }

}
