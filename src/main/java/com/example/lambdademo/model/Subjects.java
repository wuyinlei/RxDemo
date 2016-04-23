package com.example.lambdademo.model;

import java.util.List;

/**
 * Created by wuyin on 2016/4/23.
 */
public class Subjects {

    private String id;

    private String alt;

    private String year;

    private String original_title;

    private List<String> genres;

    private List<Cast> casts;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
    }


    @Override
    public String toString() {
        return "Subjects{" +
                "id='" + id + '\'' +
                ", alt='" + alt + '\'' +
                ", year='" + year + '\'' +
                ", original_title='" + original_title + '\'' +
                ", genres=" + genres +
                ", casts=" + casts +
                '}';
    }

    private class Cast {
        private String alt;
        private Avatars avatars;
        private String name;
        private String id;

        public Avatars getAvatars() {
            return avatars;
        }

        public void setAvatars(Avatars avatars) {
            this.avatars = avatars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Cast{" +
                    "alt='" + alt + '\'' +
                    ", avatars=" + avatars +
                    ", name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }

    private class Avatars {
        private String small;
        private String large;
        private String medium;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        @Override
        public String toString() {
            return "Avatars{" +
                    "small='" + small + '\'' +
                    ", large='" + large + '\'' +
                    ", medium='" + medium + '\'' +
                    '}';
        }
    }


}
