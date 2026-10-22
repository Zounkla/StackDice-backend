package zounkla.stackdice.model.dto;

import lombok.Getter;
import lombok.Setter;
import zounkla.stackdice.model.entity.Technology;

@Getter @Setter
public class Stack {
    private String projectType;
    private Technology frontend;
    private Technology backend;
    private Technology database;
    private Technology challenge;
}
