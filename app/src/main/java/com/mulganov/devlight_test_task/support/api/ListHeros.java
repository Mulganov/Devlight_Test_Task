package com.mulganov.devlight_test_task.support.api;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ListHeros {

    public String code;

    public Date data;

    public class Date {

        public ArrayList<Results> results;

        public class Results{
            public String name;
            public String description;
            public Thumbnail thumbnail;
            public Comics comics;
            public Series series;
            public ArrayList<Urls> urls;

            public class Thumbnail{
                public String path;
                public String extension;
                public String file;
            }

            public class Comics{
                public ArrayList<Items> items;
                public int available;
            }

            public class Series{
                public ArrayList<Items> items;
                public int available;
            }

            public class Items{
                public String name;
            }

            public class Urls{
                public String type;
                public String url;
            }
        }
    }

    public String toString(){
        return new Gson().toJson(this);
    }
}
