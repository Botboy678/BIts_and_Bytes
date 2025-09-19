package com.bits.bytes.bits.bytes;

import com.bits.bytes.bits.bytes.Models.Profiles;
import com.bits.bytes.bits.bytes.Models.ProjectComments;
import com.bits.bytes.bits.bytes.Models.Projects;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Factories {
    public Projects ProjectFactory(){
        return new Projects();
    }
    public ProjectComments ProjectCommentsFactory(){return new ProjectComments();}
    public Profiles ProfilesFactory(){return new Profiles();}
}
