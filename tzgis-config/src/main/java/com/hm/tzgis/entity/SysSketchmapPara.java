package com.hm.tzgis.entity;


import lombok.Data;

@Data
public class SysSketchmapPara {
    private Long id;

    private String name;

    private String value;

//    private String extname;
//
//    private String paratype;

    private Short universe;

//    private Short schemeId;
//
    private Short enabled;
//
//    private String contentoption;
//
//    private Short valuetype;
//
//    private Short showseq;

    private Long areaid;

//    private Integer groupid;
//
//    private Integer cateid;
//
//    private String insideid;
//
//    private Short showflag;
//
    private String worldId;


//	@Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(getClass().getSimpleName());
//        sb.append(" [");
//        sb.append("Hash = ").append(hashCode());
//        sb.append(", id=").append(id);
//        sb.append(", name=").append(name);
//        sb.append(", value=").append(value);
//        sb.append(", extname=").append(extname);
//        sb.append(", paratype=").append(paratype);
//        sb.append(", universe=").append(universe);
//        sb.append(", schemeId=").append(schemeId);
//        sb.append(", enabled=").append(enabled);
//        sb.append(", contentoption=").append(contentoption);
//        sb.append(", valuetype=").append(valuetype);
//        sb.append(", showseq=").append(showseq);
//        sb.append(", areaid=").append(areaid);
//        sb.append(", groupid=").append(groupid);
//        sb.append(", cateid=").append(cateid);
//        sb.append(", insideid=").append(insideid);
//        sb.append(", showflag=").append(showflag);
//        sb.append(", worldId=").append(worldId);
//        sb.append("]");
//        return sb.toString();
//    }
}