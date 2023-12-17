package com.timemanager;

public class community_class {
    String communityname;
    String communitydes;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    String owner;

    public community_class() {
    }

    public community_class(String communityname, String communitydes, String owner) {
        this.communityname = communityname;
        this.communitydes = communitydes;
        this.owner = owner;
    }

    public String getCommunityname() {
        return communityname;
    }

    public void setCommunityname(String communityname) {
        this.communityname = communityname;
    }

    public String getCommunitydes() {
        return communitydes;
    }

    public void setCommunitydes(String communitydes) {
        this.communitydes = communitydes;
    }
}
