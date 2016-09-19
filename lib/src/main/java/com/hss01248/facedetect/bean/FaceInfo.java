package com.hss01248.facedetect.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class FaceInfo {

    /**
     * face : [{"attribute":{"age":{"range":5,"value":34},"gender":{"confidence":99.8642,"value":"Male"},"race":{"confidence":92.03280000000001,"value":"Asian"},"smiling":{"value":42.1712}},"face_id":"21fb85d3f80cdf573f2c80db6728d2ff","position":{"center":{"x":57.777778,"y":52.083333},"eye_left":{"x":43.996667,"y":45.316875},"eye_right":{"x":74.991111,"y":47.150208},"height":34.583333,"mouth_left":{"x":44.844444,"y":64.589792},"mouth_right":{"x":72.467407,"y":65.045833},"nose":{"x":59.898519,"y":53.375417},"width":61.481481},"tag":""}]
     * img_height : 480
     * img_id : ccb634e58bc5dd45ffa6fd1dd6ef0cf1
     * img_width : 270
     * response_code : 200
     * session_id : 0d5a063647d64def81005cee4ac36f14
     */

    public int img_height;
    public String img_id;
    public int img_width;
    public int response_code;
    public String session_id;
    /**
     * attribute : {"age":{"range":5,"value":34},"gender":{"confidence":99.8642,"value":"Male"},"race":{"confidence":92.03280000000001,"value":"Asian"},"smiling":{"value":42.1712}}
     * face_id : 21fb85d3f80cdf573f2c80db6728d2ff
     * position : {"center":{"x":57.777778,"y":52.083333},"eye_left":{"x":43.996667,"y":45.316875},"eye_right":{"x":74.991111,"y":47.150208},"height":34.583333,"mouth_left":{"x":44.844444,"y":64.589792},"mouth_right":{"x":72.467407,"y":65.045833},"nose":{"x":59.898519,"y":53.375417},"width":61.481481}
     * tag :
     */

    public List<FaceBean> face;

    public static class FaceBean {
        /**
         * age : {"range":5,"value":34}
         * gender : {"confidence":99.8642,"value":"Male"}
         * race : {"confidence":92.03280000000001,"value":"Asian"}
         * smiling : {"value":42.1712}
         */

        public AttributeBean attribute;
        public String face_id;
        /**
         * center : {"x":57.777778,"y":52.083333}
         * eye_left : {"x":43.996667,"y":45.316875}
         * eye_right : {"x":74.991111,"y":47.150208}
         * height : 34.583333
         * mouth_left : {"x":44.844444,"y":64.589792}
         * mouth_right : {"x":72.467407,"y":65.045833}
         * nose : {"x":59.898519,"y":53.375417}
         * width : 61.481481
         */

        public PositionBean position;
        public String tag;

        public static class AttributeBean {
            /**
             * range : 5
             * value : 34
             */

            public AgeBean age;
            /**
             * confidence : 99.8642
             * value : Male
             */

            public GenderBean gender;
            /**
             * confidence : 92.03280000000001
             * value : Asian
             */

            public RaceBean race;
            /**
             * value : 42.1712
             */

            public SmilingBean smiling;

            public static class AgeBean {
                public int range;
                public int value;
            }

            public static class GenderBean {
                public double confidence;
                public String value;
            }

            public static class RaceBean {
                public double confidence;
                public String value;
            }

            public static class SmilingBean {
                public double value;
            }
        }

        public static class PositionBean {
            /**
             * x : 57.777778
             * y : 52.083333
             */

            public CenterBean center;
            /**
             * x : 43.996667
             * y : 45.316875
             */

            public EyeLeftBean eye_left;
            /**
             * x : 74.991111
             * y : 47.150208
             */

            public EyeRightBean eye_right;
            public double height;
            /**
             * x : 44.844444
             * y : 64.589792
             */

            public MouthLeftBean mouth_left;
            /**
             * x : 72.467407
             * y : 65.045833
             */

            public MouthRightBean mouth_right;
            /**
             * x : 59.898519
             * y : 53.375417
             */

            public NoseBean nose;
            public double width;

            public static class CenterBean {
                public double x;
                public double y;
            }

            public static class EyeLeftBean {
                public double x;
                public double y;
            }

            public static class EyeRightBean {
                public double x;
                public double y;
            }

            public static class MouthLeftBean {
                public double x;
                public double y;
            }

            public static class MouthRightBean {
                public double x;
                public double y;
            }

            public static class NoseBean {
                public double x;
                public double y;
            }
        }
    }
}
