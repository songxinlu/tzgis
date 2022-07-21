package com.hm.tzgis.constant;

public class Constants {

    //Geohash长度
    public static class GeoHashLength {

        public static final int FOUR = 4;//geohashcode 长度

        public static final int FIVE = 5;//geohashcode 长度

        public static final int SIX = 6;//geohashcode 长度
        
        public static final int SEVEN = 7;//geohashcode 长度
        
        public static final int EIGHT = 8;//geohashcode 长度
    }

    //shape 几何类型
    public static class Shape {

        public static final String POINT = "POINT";

        public static final String MULTIPOINT = "MULTIPOINT";

        public static final String POINT_SMALL = "Point";

        public static final String LINESTRING = "LINESTRING";

        public static final String LINESTRING_SMALL = "LineString";

        public static final String MULTILINEARRING = "MULTILINEARRING";

        public static final String MULTILINESTRING = "MULTILINESTRING";

        public static final String MULTILINESTRING_SMALL = "MultilineString";

        public static final String POLYGON = "POLYGON";

        public static final String POLYGON_SMALL = "Polygon";
    }

    //Geohash字符
    public static class GeoHashChar {

        public static final String FOUR = "hashcode4";

        public static final String FIVE = "hashcode5";

        public static final String SIX = "hashcode6";
        
        public static final String SEVEN = "hashcode7";

        public static final String EIGHT = "hashcode8";
    }

    //mongo 索引
    public static class MogoIndex {

        public static final String ASC = "ASC";

        public static final String DESC = "DESC";

        public static final String _2D = "2D";

        public static final String _2DSphere = "2DSphere";

        public static final String Hashed = "Hashed";

        public static final String Text = "Text";

    }

    //特殊字符
    public static class SpecChar {
        public static final String EMPTYNULL = "null";

        public static final String EMPTY = "";

        public static final String KG = " ";

        public static final String XHX = "_";

        public static final String AS = ",";

        public static final String ZUO = "(";

        public static final String YOU = ")";

        public static final String DATA = "data";

    }

    public static class PositionStr {

        public static final String SUBSTATION = "substation";

        public static final String LINE = "line";

        public static final String DEVICE = "device";

    }

    public static class IndexShape {

        public static final String CIRCLE = "circle";

        public static final String POLYGON = "polygon";

    }

    //河南云主站域信息表
    public static class DomainInfo {

        public static final int VOLTAGE_A = 79;//A相电压

        public static final int VOLTAGE_QUALITY_CODE_A = 80;//A相电压质量码

        public static final int VOLTAGE_B = 81;//B相电压

        public static final int VOLTAGE_QUALITY_CODE_B = 82;//B相电压质量码

        public static final int VOLTAGE_C = 83;//C相电压

        public static final int VOLTAGE_QUALITY_CODE_C = 84;//C相电压质量码

        public static final int ELECTRICITY_A = 85;//A相电流

        public static final int ELECTRICITY_QUALITY_CODE_A = 86;//A相电流质量码

        public static final int ELECTRICITY_B = 87;//B相电流

        public static final int ELECTRICITY_QUALITY_CODE_B = 88;//B相电流质量码

        public static final int ELECTRICITY_C = 89;//C相电流

        public static final int ELECTRICITY_QUALITY_CODE_C = 90;//C相电流质量码

        public static final int INSTANTANEOUS_ACTIVE_WORK = 91;//三相瞬时有功

        public static final int INSTANTANEOUS_ACTIVE_WORK_QUALITY_CODE = 92;//三相瞬时有功质量码

        public static final int ACTIVE_POWER_A = 93;//A相有功功率

        public static final int ACTIVE_POWER_QUALITY_CODE_A = 94;//A相有功功率质量码

        public static final int ACTIVE_POWER_B = 95;//B相有功功率

        public static final int ACTIVE_POWER_QUALITY_CODE_B = 96;//B相有功功率质量码

        public static final int ACTIVE_POWER_C = 97;//A相有功功率

        public static final int ACTIVE_POWER_QUALITY_CODE_C = 98;//C相有功功率质量码

        public static final int INSTANTANEOUS_REACTIVE_WORK = 99;//三相瞬时无功

        public static final int INSTANTANEOUS_REACTIVE_WORK_QUALITY_CODE = 100;//三相瞬时无功质量码

    }
}
