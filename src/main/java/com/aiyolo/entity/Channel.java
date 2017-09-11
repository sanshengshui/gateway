package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Channel extends BaseEntity {

    @Size(min=1, max=20)
    private String name;

    @OneToMany(mappedBy = "channel")
    private List<User> channelUsers;

    public Channel() {}

    public Channel(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "Channel[id=%d, name='%s']",
                id, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
